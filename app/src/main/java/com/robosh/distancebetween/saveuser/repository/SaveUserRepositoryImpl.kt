package com.robosh.distancebetween.saveuser.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.database.RealtimeDatabaseImpl
import com.robosh.distancebetween.model.Resource

class SaveUserRepositoryImpl : SaveUserRepository {

    // todo with Koin
    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabaseImpl.newInstance()

    override fun saveUser(user: User): LiveData<Resource<User>> {
        return realtimeDatabase.saveUser(user)
    }

    override fun isUserAlreadyExists(): LiveData<Resource<User>> {
        return realtimeDatabase.isUserExistsInDatabase()
    }
}