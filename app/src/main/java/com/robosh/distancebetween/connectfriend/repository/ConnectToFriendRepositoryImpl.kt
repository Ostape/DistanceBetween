package com.robosh.distancebetween.connectfriend.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.database.RealtimeDatabaseImpl
import com.robosh.distancebetween.model.User

class ConnectToFriendRepositoryImpl : ConnectToFriendRepository {

    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabaseImpl.newInstance()

    override fun getAllAvailableUsers(): LiveData<List<User>> {
        return realtimeDatabase.getAvailableUsers()
    }

    override fun setUserAvaiabilityAndAddPairedUser(id: String) {
        realtimeDatabase.setUserAvailabilityAndAddPairedUser(id)
    }
}