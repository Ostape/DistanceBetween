package com.robosh.distancebetween.waitfriend.viewmodel

import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.waitfriend.repository.WaitForFriendRepository
import com.robosh.distancebetween.waitfriend.repository.WaitForFriendRepositoryImpl

class WaitForFriendViewModel : ViewModel() {

    private val waitForFriendRepository: WaitForFriendRepository = WaitForFriendRepositoryImpl()

    fun makeCurrentUserAvailableForSharing() {
        waitForFriendRepository.makeCurrentUserAvailableForSharing()
    }

    fun makeCurrentUserNotAvailableForSharing() {
        waitForFriendRepository.makeCurrentUserNotAvailableForSharing()
    }
}