package com.robosh.distancebetween.homescreen.di

import com.robosh.distancebetween.homescreen.viewmodel.HomeScreenViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeScreenModule = module {

    viewModel { HomeScreenViewModel() }
}