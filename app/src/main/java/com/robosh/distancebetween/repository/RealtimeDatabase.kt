package com.robosh.distancebetween.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface RealtimeDatabase {

    fun isUserExistsInDatabase(): LiveData<Boolean>

    fun saveUser(user: User)

    fun saveLocation(location: Location?)

    fun getAvailableUserIds(): List<String>

    fun setUserAvailability(availability: Boolean): User
}