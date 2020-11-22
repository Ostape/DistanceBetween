package com.robosh.distancebetween.application

import com.robosh.distancebetween.model.LocationCoordinates
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val EARTH_R = 6371
const val DEFAULT_VALUE = 0.0

fun getDistanceFromLatLonInKm(
    location1: LocationCoordinates?,
    location2: LocationCoordinates?
): Double {
    if (location1 == null || location2 == null) return DEFAULT_VALUE
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
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val d = EARTH_R * c
    return d
}

private fun degreeToRadian(deg: Double): Double {
    return deg * (Math.PI / 180)
}

fun round(value: Double, places: Int): Double {
    require(places >= 0)
    var bd: BigDecimal = BigDecimal.valueOf(value)
    bd = bd.setScale(places, RoundingMode.HALF_UP)
    return bd.toDouble()
}