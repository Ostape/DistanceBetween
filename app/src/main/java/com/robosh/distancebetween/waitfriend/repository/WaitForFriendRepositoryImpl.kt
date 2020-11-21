package com.robosh.distancebetween.waitfriend.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.database.RealtimeDatabaseImpl
import com.robosh.distancebetween.model.User

class WaitForFriendRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : WaitForFriendRepository {

    override fun makeCurrentUserAvailableForSharing(): LiveData<User> {
        return realtimeDatabase.makeUserAvailableForSharing()
    }

    override fun makeCurrentUserNotAvailableForSharing() {
        realtimeDatabase.makeUserNotAvailableForSharing()
    }

    override fun getUserById(id: String): LiveData<User> {
        return realtimeDatabase.getUserById(id)
    }

    override fun rejectUserConnection() {
        realtimeDatabase.rejectUserConnection()
    }

    override fun acceptUserConnection(requestedUser: User?, currentUser: User?) {
        realtimeDatabase.acceptUserConnection(requestedUser, currentUser)
    }
}