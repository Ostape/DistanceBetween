package com.robosh.distancebetween.locationservice.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User

interface LocationRepository {

    fun getDistanceBetweenUsers():LiveData<Double>

    fun saveUserLocation(location: Location)

    fun listenUsersChanges(): LiveData<List<User>>
}