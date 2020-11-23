package com.robosh.distancebetween.waitfriend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.waitfriend.repository.WaitForFriendRepository
import com.robosh.distancebetween.waitfriend.repository.WaitForFriendRepositoryImpl

class WaitForFriendViewModel(
    private val waitForFriendRepository: WaitForFriendRepository
) : ViewModel() {

    private val requestedUser: MutableLiveData<User> = MutableLiveData()
    private val currentUser: MutableLiveData<User> = MutableLiveData()

    fun getCurrentUser(): LiveData<User> {
        return currentUser
    }

    fun waitForUserConnectionRequest(): LiveData<User> {
        return Transformations.switchMap(waitForFriendRepository.makeCurrentUserAvailableForSharing()) {
            currentUser.postValue(it)
            waitForFriendRepository.getUserById(it.connectedFriendId).also { user ->
                requestedUser.postValue(user.value)
            }
        }
    }

    fun makeCurrentUserNotAvailableForSharing() {
        waitForFriendRepository.makeCurrentUserNotAvailableForSharing()
    }

    fun rejectConnection() {
        waitForFriendRepository.rejectUserConnection()
    }

    fun acceptConnection() {
        waitForFriendRepository.acceptUserConnection(requestedUser.value, currentUser.value)
    }
}