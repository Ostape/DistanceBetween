package com.robosh.distancebetween.waitfriend.di

import com.robosh.distancebetween.waitfriend.repository.WaitForFriendRepository
import com.robosh.distancebetween.waitfriend.repository.WaitForFriendRepositoryImpl
import com.robosh.distancebetween.waitfriend.viewmodel.WaitForFriendViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val waitFriendModule = module {

    viewModel { WaitForFriendViewModel(get()) }

    single<WaitForFriendRepository> { WaitForFriendRepositoryImpl(get()) }
}