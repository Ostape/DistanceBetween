package com.robosh.distancebetween.locationscreen

import android.location.Location
import com.robosh.distancebetween.model.MyLocation

class LocationUseCase {

//    private repository: SomeRepo

    fun execute(location: Location) {
        val myLocation = MyLocation(longitude = location.longitude, latitude = location.latitude)
//        repository.saveLocation(myLocation)

    }
}