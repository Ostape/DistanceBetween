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
    private val mutableIsFormValid = MutableLiveData<Boolean>().apply { postValue(false) }

    val isFormValid: LiveData<Boolean>
        get() = mutableIsFormValid

    var username: String = ""
        set(value) {
            field = value
            validateUsername()
        }

    fun saveUser() {
        saveUserRepository.saveUser(User(username = username))
    }

    fun isUserExistsInDatabase(): LiveData<Boolean> {
        return saveUserRepository.isUserExists()
    }

    private fun validateUsername() {
        if (username.length > 5) {
            mutableIsFormValid.postValue(true)
        } else {
            mutableIsFormValid.postValue(false)
        }
    }
}