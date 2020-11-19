package com.robosh.distancebetween.saveuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.application.EMPTY_STRING
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.saveuser.repository.SaveUserRepository

class SaveUserViewModel(
    private val saveUserRepository: SaveUserRepository
) : ViewModel() {

    private companion object {
        const val MINIMUM_USERNAME_LENGTH = 3
    }

    private val mutableIsFormValid = MutableLiveData<Boolean>().apply { postValue(false) }

    val isFormValid: LiveData<Boolean>
        get() = mutableIsFormValid

    var username: String = EMPTY_STRING
        set(value) {
            field = value
            validateUsername()
        }

    fun saveUser(): LiveData<Resource<User>> {
        return saveUserRepository.saveUser(User(username = username))
    }

    fun isUserExistsInDatabase(): LiveData<Resource<User>> {
        return saveUserRepository.isUserAlreadyExists()
    }

    private fun validateUsername() {
        if (username.length > MINIMUM_USERNAME_LENGTH) {
            mutableIsFormValid.postValue(true)
        } else {
            mutableIsFormValid.postValue(false)
        }
    }
}