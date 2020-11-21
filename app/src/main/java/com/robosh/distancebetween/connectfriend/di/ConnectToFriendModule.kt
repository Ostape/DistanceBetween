package com.robosh.distancebetween.connectfriend.di

import com.robosh.distancebetween.connectfriend.repository.ConnectToFriendRepository
import com.robosh.distancebetween.connectfriend.repository.ConnectToFriendRepositoryImpl
import com.robosh.distancebetween.connectfriend.viewmodel.ConnectToFriendViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val connectToFriendModule = module {

    viewModel { ConnectToFriendViewModel(get()) }

    single<ConnectToFriendRepository> { ConnectToFriendRepositoryImpl(get()) }
}