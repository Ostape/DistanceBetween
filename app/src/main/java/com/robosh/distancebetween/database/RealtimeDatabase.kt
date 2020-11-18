package com.robosh.distancebetween.database

import android.location.Location
import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface RealtimeDatabase {

    fun isUserExistsInDatabase(): LiveData<User>

    fun saveUser(user: User)

    fun saveLocation(location: Location?)

    fun getAvailableUsers(): LiveData<List<User>>

    fun setUserAvailability(availability: Boolean): User

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
}