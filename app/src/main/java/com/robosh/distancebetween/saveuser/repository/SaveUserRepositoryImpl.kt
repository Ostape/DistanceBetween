package com.robosh.distancebetween.saveuser.repository

import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.repository.RealtimeDatabase
import com.robosh.distancebetween.repository.RealtimeDatabaseImpl

class SaveUserRepositoryImpl : SaveUserRepository {

    // todo with Koin
    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabaseImpl.newInstance()

    override fun saveUser(user: User): User {
        return realtimeDatabase.saveUser(user)
    }
}