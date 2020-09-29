package com.robosh.distancebetween.locationservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ForegroundLocationService : Service() {

    private val localBinder: Binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return localBinder
    }

    inner class LocalBinder : Binder() {
        internal val service: ForegroundLocationService
            get() = this@ForegroundLocationService
    }
}