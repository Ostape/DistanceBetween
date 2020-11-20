package com.robosh.distancebetween.locationservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.google.android.gms.location.*
import com.robosh.distancebetween.MainActivity
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.*
import com.robosh.distancebetween.locationservice.repository.LocationRepository
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.widget.LocationWidgetProvider
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ForegroundLocationService : LifecycleService() {

    private val locationRepository: LocationRepository by inject()

    private lateinit var notificationManager: NotificationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var isFirstRun = true
    private var cachedUser: User? = null

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(10)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationRepository.getDistanceBetweenUsers().observe(this, Observer {
            sendWidgetBroadcast(it)
        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult?.lastLocation != null) {
                    locationRepository.saveUserLocation(locationResult.lastLocation)
                } else {
                    Timber.d("Location information isn't available.")
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        cachedUser = intent?.getParcelableExtra(INTENT_USER_FOR_SERVICE)
        Timber.d("TAGGERR" +cachedUser.toString())
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }
                }
                ACTION_STOP_SERVICE -> {
                    stopForeground(true)
                    stopSelf()
                }
            }
        }

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

    private fun startForegroundService() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val titleText = "Title notification"
        val mainNotificationText = "Notification message"
        createNotificationChannel(titleText)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        val notificationBuilder =
            getNotificationBuilder(bigTextStyle, titleText, mainNotificationText)

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationBuilder(
        bigTextStyle: NotificationCompat.BigTextStyle?,
        titleText: String,
        mainNotificationText: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setAutoCancel(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Launch Activity",
                getMainActivityPendingIntent()
            )
    }

    private fun createNotificationChannel(titleText: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                titleText,
                IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getMainActivityPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).also
            { it.action = ACTION_SHOW_DISTANCE_BETWEEN_FRAGMENT },
            FLAG_UPDATE_CURRENT
        )
    }

    private fun sendWidgetBroadcast(distance: Double) {
        val thisWidget = ComponentName(applicationContext, LocationWidgetProvider::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(this.applicationContext)
        val allWidgetIds: IntArray = appWidgetManager.getAppWidgetIds(thisWidget)

        for (widgetId in allWidgetIds) {
            val remoteViews =
                RemoteViews(this.applicationContext.packageName, R.layout.widget_location)
            remoteViews.setTextViewText(R.id.exampleWidgetButton, "Random: $distance")
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }
}