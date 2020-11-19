package com.robosh.distancebetween.saveuser.di

import com.robosh.distancebetween.saveuser.repository.SaveUserRepository
import com.robosh.distancebetween.saveuser.repository.SaveUserRepositoryImpl
import com.robosh.distancebetween.saveuser.viewmodel.SaveUserViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val saveUserModule = module {

    // ViewModel instance of MyViewModel
    // get() will resolve Repository instance
    viewModel { SaveUserViewModel(get()) }

    // Single instance of Repository
    single<SaveUserRepository> { SaveUserRepositoryImpl(get()) }
}