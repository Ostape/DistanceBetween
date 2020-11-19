package com.robosh.distancebetween.locationservice.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.locationservice.repository.LocationRepository
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User

class LocationViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

    fun saveLocation(location: Location): LiveData<Resource<User>> {
        val locationCoordinates = LocationCoordinates(location.latitude, location.longitude)
        return locationRepository.saveUserLocation(locationCoordinates)
    }

    fun getDistanceBetweenUsers(currentUser: User): LiveData<Double> {
        locationRepository.listenCurrentUserChanges()
        locationRepository.listenConnectedUserChanges(currentUser.connectedFriendId)
        return MutableLiveData<Double>().apply { postValue(100.0) }
    }
}