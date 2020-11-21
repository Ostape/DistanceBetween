package com.robosh.distancebetween.database

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User

interface RealtimeDatabase {

    fun isUserExistsInDatabase(): LiveData<Resource<User>>

    fun saveUser(user: User): LiveData<Resource<User>>

    fun saveLocation(locationCoordinates: LocationCoordinates)

    fun getAvailableUsers(): LiveData<List<User>>

    fun setUserAvailabilityAndAddPairedUser(id: String)

    fun makeUserAvailableForSharing(): LiveData<User>

    fun makeUserNotAvailableForSharing()

    fun getUserById(id: String): LiveData<User>

    fun rejectUserConnection()

    fun acceptUserConnection(
        requestedUser: User?,
        currentUser: User?
    )

    fun getCurrentUser(): LiveData<User>

    fun listenUserChanges(connectedUserId: String): LiveData<List<User>>
}