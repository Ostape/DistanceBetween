package com.robosh.distancebetween.locationservice.di

import com.robosh.distancebetween.locationservice.repository.LocationRepository
import com.robosh.distancebetween.locationservice.repository.LocationRepositoryImpl
import com.robosh.distancebetween.locationservice.viewmodel.LocationViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationModule = module {

    viewModel { LocationViewModel(get()) }

    single<LocationRepository> { LocationRepositoryImpl(get()) }
}