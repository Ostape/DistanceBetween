package com.robosh.distancebetween.locationservice.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.User

class LocationRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : LocationRepository {

    override fun saveUserLocation(location: Location) {
        val locationCoordinates = LocationCoordinates(location.latitude, location.longitude)
        realtimeDatabase.saveLocation(locationCoordinates)
    }

    override fun listenUsersChanges(connectedUserId: String): LiveData<List<User>> {
        return realtimeDatabase.listenUserChanges(connectedUserId)
    }
}