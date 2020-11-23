package com.robosh.distancebetween.locationservice.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.robosh.distancebetween.database.RealtimeDatabase
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.User

class LocationRepositoryImpl(
    private val realtimeDatabase: RealtimeDatabase
) : LocationRepository {

    private val mutableCurrentUser = MutableLiveData<User>()

    private val currentUser: LiveData<User>
        get() = mutableCurrentUser

    override fun saveUserLocation(location: Location) {
        val locationCoordinates = LocationCoordinates(location.latitude, location.longitude)
        realtimeDatabase.saveLocation(locationCoordinates)
    }

    override fun listenUsersChanges(): LiveData<List<User>> {
        return Transformations.switchMap(realtimeDatabase.getCurrentUser()) {
            mutableCurrentUser.postValue(it)
            realtimeDatabase.listenUserChanges(it.connectedFriendId)
        }
    }
}