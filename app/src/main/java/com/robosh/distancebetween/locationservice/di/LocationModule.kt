package com.robosh.distancebetween.locationservice.di

import com.robosh.distancebetween.locationservice.repository.LocationRepository
import com.robosh.distancebetween.locationservice.repository.LocationRepositoryImpl
import org.koin.dsl.module

val locationModule = module {

    single<LocationRepository> { LocationRepositoryImpl(get()) }
}