package com.robosh.distancebetween.homescreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeScreenViewModel : ViewModel() {

    private val mutableIsPermissionGranted = MutableLiveData<Boolean>().apply { postValue(false) }

    val isPermissionGranted: LiveData<Boolean>
        get() = mutableIsPermissionGranted

    fun setIsPermissionGranted(isGranted: Boolean) {
        mutableIsPermissionGranted.postValue(isGranted)
    }
}