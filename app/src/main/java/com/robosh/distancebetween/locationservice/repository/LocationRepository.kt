package com.robosh.distancebetween.locationservice.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface LocationRepository {

    fun saveUserLocation(location: Location)

    fun listenUsersChanges(): LiveData<List<User>>

    fun getCurrentUser(): LiveData<User>
}