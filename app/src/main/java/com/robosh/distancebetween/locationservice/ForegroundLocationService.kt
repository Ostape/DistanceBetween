package com.robosh.distancebetween.locationservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.robosh.distancebetween.DatabaseRepository
import com.robosh.distancebetween.MainActivity
import com.robosh.distancebetween.R
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ForegroundLocationService : Service() {

    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_ID = 110
    }

    private lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e., how often you should receive
    // updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback

    // Used only for local storage of the last known location. Usually, this would be saved to your
    // database, but because this is a simplified sample without a full database, we only need the
    // last location to create a Notification if the user navigates away from the app.
    private var currentLocation: Location? = null
    private val localBinder: Binder = LocalBinder()
    private var configurationChange = false
    private var serviceRunningInForeground = false


    // TODO add check if GPS is on/off
    override fun onCreate() {
        super.onCreate()
        Timber.d("Service onCreate Callback")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            //
            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
            // targetSdkVersion) may receive updates less frequently than this interval when the app
            // is no longer in the foreground.
            interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(10)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {
                    // Normally, you want to save a new location to a database. We are simplifying
                    // things a bit and just saving it as a local variable, as we only need it again
                    // if a Notification is created (when user navigates away from app).
                    currentLocation = locationResult.lastLocation

                    // Notify our Activity that a new location was added. Again, if this was a
                    // production app, the Activity would be listening for changes to a database
                    // with new locations, but we are simplifying things a bit to focus on just
                    // learning the location side of things.

                    // TODO save to DB
                    DatabaseRepository.newInstance().saveLocation(currentLocation)
//                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
//                    intent.putExtra(EXTRA_LOCATION, currentLocation)
//                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                    // Updates notification content if this service is running as a foreground
                    // service.
                    if (serviceRunningInForeground) {
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            createNotification(currentLocation)
                        )
                    }
                } else {
                    Timber.d("Location information isn't available.")
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Service onStartCommand Callback")
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("Service onBind method")

        stopForeground(true)
        configurationChange = false
        serviceRunningInForeground = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Timber.d("Service onRebind method")

        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d("Service onUnbind method")

        if (configurationChange.not()) {
            val notification = createNotification(currentLocation)
            startForeground(NOTIFICATION_ID, notification)
            serviceRunningInForeground = true
        }
        return true
    }

    fun onSubscribe() {
        Timber.d("OnSubscribe to location changes")
        startService(Intent(applicationContext, ForegroundLocationService::class.java))

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } catch (exception: SecurityException) {
            Timber.e("Lost location permissions. Couldn't remove updates. $exception")
        }
    }

    fun onUnSubscribe() {
        Timber.d("onUnSubscribe from location changes")
        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Location Callback removed.")
                    stopSelf()
                } else {
                    Timber.d("Failed to remove Location Callback.")
                }
            }
        } catch (exception: SecurityException) {
            Timber.e("Lost location permissions. Couldn't remove updates. $exception")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    private fun createNotification(location: Location?): Notification {
        Timber.d("Creating Notification")
        val titleText = "Title notification"
        val mainNotificationText = "Notification message ${location?.latitude}"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        val launchActivityIntent = Intent(this, MainActivity::class.java)

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, 0
        )

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_launcher_foreground, "Launch Activity",
                activityPendingIntent
            )
            .build()
    }

    inner class LocalBinder : Binder() {
        internal val service: ForegroundLocationService
            get() = this@ForegroundLocationService
    }
}