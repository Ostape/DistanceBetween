package com.robosh.distancebetween.database.di

import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.database.RealtimeDatabaseImpl
import org.koin.dsl.module

val databaseModule = module {
    
    single<RealtimeDatabase> { RealtimeDatabaseImpl() }
}