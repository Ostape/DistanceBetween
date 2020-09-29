package com.robosh.distancebetween.application

import android.app.Application
import com.robosh.distancebetween.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


class RoboshApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}