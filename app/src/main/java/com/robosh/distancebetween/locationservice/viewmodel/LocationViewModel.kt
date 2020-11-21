package com.robosh.distancebetween.locationservice.viewmodel

import android.location.Location
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.application.getDistanceFromLatLonInKm
import com.robosh.distancebetween.locationservice.repository.LocationRepository
import com.robosh.distancebetween.model.LocationCoordinates

class LocationViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

//    private val usersData = locationRepository.listenUsersChanges()

}