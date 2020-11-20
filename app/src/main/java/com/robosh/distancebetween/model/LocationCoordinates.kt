package com.robosh.distancebetween.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationCoordinates(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Parcelable