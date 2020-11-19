package com.robosh.distancebetween.locationservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.android.gms.location.*
import com.robosh.distancebetween.MainActivity
import com.robosh.distancebetween.R
import com.robosh.distancebetween.widget.LocationWidgetProvider
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ForegroundLocationService : LifecycleService() {

    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_ID = 110
    }

    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    // TODO add check if GPS is on/off
    override fun onCreate() {
        super.onCreate()
        Timber.d("Service onCreate Callback")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(10)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult?.lastLocation != null) {
                    currentLocation = locationResult.lastLocation

                    // TODO save to DB
                    sendWidgetBroadcast(currentLocation)
                    notificationManager.notify(
                        NOTIFICATION_ID,
                        createNotification(currentLocation)
                    )
                } else {
                    Timber.d("Location information isn't available.")
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.d("Service onStartCommand Callback")
        val notification = createNotification(currentLocation)
        startForeground(NOTIFICATION_ID, notification)
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } catch (exception: SecurityException) {
            Timber.e("Lost location permissions. Couldn't remove updates. $exception")
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        Timber.d("Service onBind method")
        return null
    }

    override fun onDestroy() {
        val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("Location Callback removed.")
                stopSelf()
            } else {
                Timber.d("Failed to remove Location Callback.")
            }
        }
        super.onDestroy()
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

    private fun sendWidgetBroadcast(location: Location?) {
        val thisWidget = ComponentName(applicationContext, LocationWidgetProvider::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(this.applicationContext)
        val allWidgetIds: IntArray = appWidgetManager.getAppWidgetIds(thisWidget)

        for (widgetId in allWidgetIds) {
            val remoteViews =
                RemoteViews(this.applicationContext.packageName, R.layout.widget_location)
            remoteViews.setTextViewText(R.id.exampleWidgetButton, "Random: ${location?.latitude}")
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }
}