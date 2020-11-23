package com.robosh.distancebetween.locationscreen.repository

import androidx.lifecycle.LiveData
import com.robosh.distancebetween.model.User

interface LocationDistanceRepository {

    fun listenUsersChanges(connectedUserId: String): LiveData<List<User>>
}