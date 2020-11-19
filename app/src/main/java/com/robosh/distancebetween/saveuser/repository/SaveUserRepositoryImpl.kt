package com.robosh.distancebetween.saveuser.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User

class SaveUserRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : SaveUserRepository {

    override fun saveUser(user: User): LiveData<Resource<User>> {
        return realtimeDatabase.saveUser(user)
    }

    override fun isUserAlreadyExists(): LiveData<Resource<User>> {
        return realtimeDatabase.isUserExistsInDatabase()
    }
}