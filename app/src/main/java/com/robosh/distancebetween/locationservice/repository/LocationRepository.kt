package com.robosh.distancebetween.locationservice.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User

interface LocationRepository {

    fun saveUserLocation(location: LocationCoordinates): LiveData<Resource<User>>

    fun listenConnectedUserChanges(connectedFriendId: String): LiveData<Resource<User>>

    fun listenCurrentUserChanges(): LiveData<Resource<User>>
}