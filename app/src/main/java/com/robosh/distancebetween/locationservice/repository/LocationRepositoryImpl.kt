package com.robosh.distancebetween.locationservice.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User

class LocationRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : LocationRepository{

    override fun saveUserLocation(location: LocationCoordinates): LiveData<Resource<User>> {
        TODO("Not yet implemented")
    }

    override fun listenConnectedUserChanges(connectedFriendId: String): LiveData<Resource<User>> {
        TODO("Not yet implemented")
    }

    override fun listenCurrentUserChanges(): LiveData<Resource<User>> {
        TODO("Not yet implemented")
    }
}