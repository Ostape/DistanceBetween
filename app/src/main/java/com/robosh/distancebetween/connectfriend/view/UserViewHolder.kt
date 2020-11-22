package com.robosh.distancebetween.connectfriend.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.robosh.distancebetween.databinding.AvailableUserViewHolderBinding
import com.robosh.distancebetween.model.User

class UserViewHolder(
    view: View,
    private val connectFriendButtonCallback: ConnectFriendButtonCallback
) : RecyclerView.ViewHolder(view) {

    private val binding: AvailableUserViewHolderBinding = AvailableUserViewHolderBinding.bind(view)

    fun bind(user: User) {
        binding.userId.text = user.id
        binding.usernameTextView.text = user.username
        binding.shareLocationButton.setOnClickListener {
            connectFriendButtonCallback.onConnectFriend(user.id)
        }
    }
}