package com.robosh.distancebetween.model

import android.location.Location
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String = "",
    var username: String? = "",
    var isUserAvailable: Boolean = false,
    var location: Location? = null
)