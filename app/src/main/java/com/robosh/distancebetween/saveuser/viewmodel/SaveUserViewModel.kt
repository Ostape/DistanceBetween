package com.robosh.distancebetween.saveuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.saveuser.repository.SaveUserRepository
import com.robosh.distancebetween.saveuser.repository.SaveUserRepositoryImpl

class SaveUserViewModel : ViewModel() {

    // todo singleton with Koin
    private val saveUserRepository: SaveUserRepository = SaveUserRepositoryImpl()

    // todo rename
    private val misFormValid = MutableLiveData<Boolean>().apply { postValue(false) }

    val isFormValid: LiveData<Boolean>
        get() = misFormValid

    var username: String = ""
        set(value) {
            field = value
            validateUsername()
        }

    fun saveUser() {
        saveUserRepository.saveUser(User())
    }

    private fun validateUsername() {
        if (username.length > 5) {
            misFormValid.postValue(true)
        } else {
            misFormValid.postValue(false)
        }
    }
}