package com.robosh.distancebetween.connectfriend.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface ConnectToFriendRepository {

    fun getAllAvailableUsers(): LiveData<List<User>>

    fun setUserAvailabilityAndAddPairedUser(id: String)

    fun getCurrentUser(): LiveData<User>
}