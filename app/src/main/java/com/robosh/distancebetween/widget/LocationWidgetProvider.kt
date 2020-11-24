package com.robosh.distancebetween.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.robosh.distancebetween.mainactivity.view.MainActivity
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.ACTION_SHOW_DISTANCE_BETWEEN_FRAGMENT

class LocationWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, MainActivity::class.java).also {
                it.action = ACTION_SHOW_DISTANCE_BETWEEN_FRAGMENT
            }
            PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val remoteViews = RemoteViews(context.packageName, R.layout.widget_location)
            remoteViews.setOnClickPendingIntent(R.id.widgetLayoutId, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
}