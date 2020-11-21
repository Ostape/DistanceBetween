package com.robosh.distancebetween.locationservice.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface LocationRepository {

    fun getDistanceBetweenUsers():LiveData<Double>

    fun saveUserLocation(location: Location)

    fun listenUsersChanges(connectedUserId: String): LiveData<List<User>>
}