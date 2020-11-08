package com.robosh.distancebetween

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.robosh.distancebetween.locationservice.ForegroundLocationService

class MainActivity : AppCompatActivity() {

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundLocationService? = null

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
    }


//
//    private fun onStopReceiveLocationUpdates() {
//        foregroundOnlyLocationService?.onUnSubscribe()
//    }
//
//    private fun onStartReceiveLocationUpdates() {
//        foregroundOnlyLocationService?.onSubscribe()
//    }

    //////////////////////////////////////////////
//    private fun sendWidgetBroadcast() {
//        val thisWidget = ComponentName(applicationContext, LocationWidgetProvider::class.java)
//        val appWidgetManager = AppWidgetManager.getInstance(this.applicationContext)
//        val allWidgetIds: IntArray = appWidgetManager.getAppWidgetIds(thisWidget)
//
//        for (widgetId in allWidgetIds) {
//            // create some random data
//            val remoteViews =
//                RemoteViews(this.applicationContext.packageName, R.layout.widget_location)
//            // Set the text
//            remoteViews.setTextViewText(R.id.exampleWidgetButton, "Random: $testInt")
//            testInt++
//            // Register an onClickListener
//            appWidgetManager.updateAppWidget(widgetId, remoteViews)
//        }
//    }
}