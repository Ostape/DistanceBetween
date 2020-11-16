package com.robosh.distancebetween.waitfriend.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface WaitForFriendRepository {

    fun makeCurrentUserAvailableForSharing(): LiveData<User>

    fun makeCurrentUserNotAvailableForSharing()

    fun getUserById(id: String): LiveData<User>

    fun rejectUserConnection()

    fun acceptUserConnection(requestedUser: User?, currentUser: User?)
}