package com.robosh.distancebetween.locationservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.robosh.distancebetween.MainActivity
import com.robosh.distancebetween.R
import timber.log.Timber

class ForegroundLocationService : Service() {

    private val localBinder: Binder = LocalBinder()
    private var configurationChange = false


    private lateinit var notificationManager: NotificationManager

    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_ID = 110

    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("Service onCreate Callback")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Service onStartCommand Callback")
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("Service onBind method")

        stopForeground(true)
        configurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Timber.d("Service onRebind method")

        stopForeground(true)
//        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d("Service onUnbind method")

        if (configurationChange.not()) {
            val notification = createNotification()
            startForeground(NOTIFICATION_ID, notification)
        }
        return true
    }

    fun onSubscribe() {
        Timber.d("OnSubscribe to location changes")
        startService(Intent(applicationContext, ForegroundLocationService::class.java))
    }

    fun onUnSubscribe() {
        Timber.d("onUnSubscribe from location changes")
        stopSelf()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }


    private fun createNotification(): Notification {
        Timber.d("Creating Notification")
        val titleText = "Title notification"
        val mainNotificationText = "Notification message"

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

//        val cancelIntent = Intent(this, ForegroundLocationService::class.java)
//        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)

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
//            .addAction(
//                R.drawable.ic_cancel,
//                "Stop service",
//                servicePendingIntent
//            )
            .build()
    }

    inner class LocalBinder : Binder() {
        internal val service: ForegroundLocationService
            get() = this@ForegroundLocationService
    }
}