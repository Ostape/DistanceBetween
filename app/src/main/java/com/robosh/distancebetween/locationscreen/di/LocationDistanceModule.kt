package com.robosh.distancebetween.locationscreen.di

import com.robosh.distancebetween.locationscreen.repository.LocationDistanceRepository
import com.robosh.distancebetween.locationscreen.repository.LocationDistanceRepositoryImpl
import com.robosh.distancebetween.locationscreen.viewmodel.LocationDistanceViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationDistanceModule = module {

    viewModel { LocationDistanceViewModel(get()) }

    single<LocationDistanceRepository> { LocationDistanceRepositoryImpl(get()) }
}