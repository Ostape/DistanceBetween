package com.robosh.distancebetween.connectfriend.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.model.User

class ConnectToFriendRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : ConnectToFriendRepository {

    override fun getAllAvailableUsers(): LiveData<List<User>> {
        return realtimeDatabase.getAvailableUsers()
    }

    override fun setUserAvailabilityAndAddPairedUser(id: String) {
        realtimeDatabase.setUserAvailabilityAndAddPairedUser(id)
    }

    override fun getCurrentUser(): LiveData<User> {
        return realtimeDatabase.getCurrentUser()
    }
}