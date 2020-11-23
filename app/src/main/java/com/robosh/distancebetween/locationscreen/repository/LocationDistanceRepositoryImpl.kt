package com.robosh.distancebetween.locationscreen.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.model.User

class LocationDistanceRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : LocationDistanceRepository {

    override fun listenUsersChanges(connectedUserId: String): LiveData<List<User>> {
        return realtimeDatabase.listenUserChanges(connectedUserId)
    }

    override fun stopSharingLocation(cachedUser: User?) {
        realtimeDatabase.stopSharingLocation(cachedUser)
    }

    override fun getCurrentUser(): LiveData<User> {
        return realtimeDatabase.getCurrentUser()
    }
}