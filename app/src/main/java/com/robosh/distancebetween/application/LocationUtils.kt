package com.robosh.distancebetween.application

import com.robosh.distancebetween.model.LocationCoordinates
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val EARTH_R = 6371

fun getDistanceFromLatLonInKm(location1: LocationCoordinates, location2: LocationCoordinates): Double {
    val dLat =
        degreeToRadian(location2.latitude - location1.latitude)
    val dLon =
        degreeToRadian(location2.longitude - location1.longitude)
    val a =
        sin(dLat / 2) * sin(dLat / 2) +
                cos(
                    degreeToRadian(
                        location1.latitude
                    )
                ) * cos(
            degreeToRadian(
                location2.latitude
            )
        ) *
                sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1-a))
    val d = EARTH_R * c
    return d
}

private fun degreeToRadian(deg: Double): Double {
    return deg * (Math.PI / 180)
}