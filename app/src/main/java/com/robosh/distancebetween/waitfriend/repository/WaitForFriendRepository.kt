package com.robosh.distancebetween.waitfriend.repository

import com.robosh.distancebetween.model.User

interface WaitForFriendRepository {

    fun makeCurrentUserAvailableForSharing()

    fun makeCurrentUserNotAvailableForSharing()
}