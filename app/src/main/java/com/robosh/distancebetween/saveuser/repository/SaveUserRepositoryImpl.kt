package com.robosh.distancebetween.saveuser.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.repository.RealtimeDatabase
import com.robosh.distancebetween.repository.RealtimeDatabaseImpl

class SaveUserRepositoryImpl : SaveUserRepository {

    // todo with Koin
    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabaseImpl.newInstance()

    override fun saveUser(user: User): User {
        return realtimeDatabase.saveUser(user)
    }

    override fun isUserExists(): LiveData<Boolean> {
        return realtimeDatabase.isUserExistsInDatabase()
    }
}