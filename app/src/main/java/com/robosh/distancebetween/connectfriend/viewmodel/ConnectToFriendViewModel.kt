package com.robosh.distancebetween.connectfriend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.robosh.distancebetween.connectfriend.repository.ConnectToFriendRepository
import com.robosh.distancebetween.connectfriend.repository.ConnectToFriendRepositoryImpl
import com.robosh.distancebetween.model.User

class ConnectToFriendViewModel : ViewModel() {

    private val connectToFriendRepository: ConnectToFriendRepository =
        ConnectToFriendRepositoryImpl()

    fun getAllAvailableUsers(): LiveData<List<User>> {
        return connectToFriendRepository.getAllAvailableUsers()
    }
}