package com.robosh.distancebetween.locationscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.locationscreen.repository.LocationDistanceRepository
import com.robosh.distancebetween.model.User

class LocationDistanceViewModel(
    private val locationDistanceRepository: LocationDistanceRepository
) : ViewModel() {

    fun listenUsersChanges(connectedUserId: String): LiveData<List<User>> {
        return locationDistanceRepository.listenUsersChanges(connectedUserId)
    }

    fun stopShareLocation(cachedUser: User?) {
        locationDistanceRepository.stopSharingLocation(cachedUser)
    }

    fun getCurrentUser(): LiveData<User> {
        return locationDistanceRepository.getCurrentUser()
    }
}