package com.robosh.distancebetween.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username: String = "",
    var isUserAvailable: Boolean = false,
    var locationCoordinates: LocationCoordinates? = null,
    var connectedFriendId: String = ""
) {
    var id: String = ""
}