package com.robosh.distancebetween.saveuser.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface SaveUserRepository {

    fun saveUser(user: User)

    fun isUserExists(): LiveData<Boolean>
}