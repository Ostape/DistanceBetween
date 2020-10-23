package com.robosh.distancebetween.model

import android.location.Location
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String = "",
    var name: String? = "",
    var surname: String? = "",
    var isUserAvailable: Boolean = true,
    var location: Location? = null
)