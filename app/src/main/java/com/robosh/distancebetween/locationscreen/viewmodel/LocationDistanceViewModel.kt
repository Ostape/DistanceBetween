package com.robosh.distancebetween.locationscreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.locationscreen.repository.LocationDistanceRepository
import com.robosh.distancebetween.model.User

class LocationDistanceViewModel(
    private val locationDistanceRepository: LocationDistanceRepository
) : ViewModel() {

    private val mutableCurrentUser = MutableLiveData<User>()

    val currentUser: LiveData<User>
        get() = mutableCurrentUser

    fun listenUsersChanges(): LiveData<List<User>> {
        return Transformations.switchMap(locationDistanceRepository.getCurrentUser()) {
            mutableCurrentUser.postValue(it)
            locationDistanceRepository.listenUsersChanges(it.connectedFriendId)
        }
    }

    fun stopShareLocation(cachedUser: User?) {
        locationDistanceRepository.stopSharingLocation(cachedUser)
    }
}