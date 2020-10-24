package com.robosh.distancebetween.repository

import android.location.Location
import com.robosh.distancebetween.model.User

interface DatabaseRepository {

    fun isUserExistsInDatabase(userId: String): Boolean

    fun saveUser(user: User): User

    fun saveLocation(location: Location?)

    fun getAvailableUserIds(): List<String>

    fun setUserAvailability(availability: Boolean): User
}