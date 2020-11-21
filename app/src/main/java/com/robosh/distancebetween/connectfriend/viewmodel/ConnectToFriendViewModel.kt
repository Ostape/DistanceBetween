package com.robosh.distancebetween.connectfriend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.connectfriend.repository.ConnectToFriendRepository
import com.robosh.distancebetween.model.User

class ConnectToFriendViewModel(
    private val connectToFriendRepository: ConnectToFriendRepository
) : ViewModel() {

    fun getAllAvailableUsers(): LiveData<List<User>> {
        return connectToFriendRepository.getAllAvailableUsers()
    }

    fun pairConnectedUser(id: String?) {
        connectToFriendRepository.setUserAvailabilityAndAddPairedUser(id!!)
    }

    fun getCurrentUser(): LiveData<User> {
        return connectToFriendRepository.getCurrentUser()
    }
}