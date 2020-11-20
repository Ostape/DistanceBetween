package com.robosh.distancebetween.locationservice.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.robosh.distancebetween.application.getDistanceFromLatLonInKm
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.User

class LocationRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : LocationRepository {

    private val distance = MediatorLiveData<Double>()

    init {
        distance.addSource(listenUsersChanges()) { result ->
            result?.let { users ->
                // todo refactor
                if (users.size > 1) {
                    distance.value = getDistanceFromLatLonInKm(
                        users[0].locationCoordinates,
                        users[1].locationCoordinates
                    )
                }
            }
        }
    }

    override fun getDistanceBetweenUsers(): LiveData<Double> {
        return distance
    }

    override fun saveUserLocation(location: Location) {
        val locationCoordinates = LocationCoordinates(location.latitude, location.longitude)
        realtimeDatabase.saveLocation(locationCoordinates)
    }

    override fun listenUsersChanges(): LiveData<List<User>> {
        return realtimeDatabase.listenUserChanges()
    }
}