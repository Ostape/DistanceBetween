package com.robosh.distancebetween

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

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