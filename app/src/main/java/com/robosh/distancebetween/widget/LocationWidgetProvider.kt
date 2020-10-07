package com.robosh.distancebetween.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.robosh.distancebetween.MainActivity
import com.robosh.distancebetween.R

class LocationWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val remoteViews = RemoteViews(context.packageName, R.layout.widget_location)
            remoteViews.setOnClickPendingIntent(R.id.exampleWidgetButton, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
}