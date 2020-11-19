package com.robosh.distancebetween.saveuser.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User

interface SaveUserRepository {

    fun saveUser(user: User): LiveData<Resource<User>>

    fun isUserExists(): LiveData<User>
}