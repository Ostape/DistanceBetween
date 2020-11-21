package com.robosh.distancebetween.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class User(
    var username: String = "",
    var isUserAvailable: Boolean = false,
    var location: LocationCoordinates? = null,
    var connectedFriendId: String = ""
) : Parcelable {

    @IgnoredOnParcel
    var id: String = ""
}