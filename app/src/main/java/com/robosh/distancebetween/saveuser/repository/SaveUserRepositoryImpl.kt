package com.robosh.distancebetween.saveuser.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.database.RealtimeDatabaseImpl

class SaveUserRepositoryImpl : SaveUserRepository {

    // todo with Koin
    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabaseImpl.newInstance()

    override fun saveUser(user: User) {
        realtimeDatabase.saveUser(user)
    }

    override fun isUserExists(): LiveData<User> {
        return realtimeDatabase.isUserExistsInDatabase()
    }
}