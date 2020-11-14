package com.robosh.distancebetween.waitfriend.repository

import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.database.RealtimeDatabaseImpl

class WaitForFriendRepositoryImpl : WaitForFriendRepository {

    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabaseImpl.newInstance()

    override fun makeCurrentUserAvailableForSharing() {
        realtimeDatabase.makeUserAvailableForSharing()
    }

    override fun makeCurrentUserNotAvailableForSharing() {
        realtimeDatabase.makeUserNotAvailableForSharing()
    }
}