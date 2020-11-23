package com.robosh.distancebetween.application

import android.app.Application
import com.robosh.distancebetween.BuildConfig
import com.robosh.distancebetween.connectfriend.di.connectToFriendModule
import com.robosh.distancebetween.database.di.databaseModule
import com.robosh.distancebetween.homescreen.di.homeScreenModule
import com.robosh.distancebetween.locationscreen.di.locationDistanceModule
import com.robosh.distancebetween.locationservice.di.locationModule
import com.robosh.distancebetween.saveuser.di.saveUserModule
import com.robosh.distancebetween.waitfriend.di.waitFriendModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class RoboshApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@RoboshApplication)
            modules(
                saveUserModule,
                databaseModule,
                locationModule,
                connectToFriendModule,
                waitFriendModule,
                homeScreenModule,
                locationDistanceModule
            )
        }
    }
}