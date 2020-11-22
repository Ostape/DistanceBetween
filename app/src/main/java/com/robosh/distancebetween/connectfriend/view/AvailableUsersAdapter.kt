package com.robosh.distancebetween.connectfriend.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robosh.distancebetween.R
import com.robosh.distancebetween.model.User

class AvailableUsersAdapter(
    private val connectFriendButtonCallback: ConnectFriendButtonCallback
) : RecyclerView.Adapter<UserViewHolder>() {

    private val users: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.available_user_view_holder, parent, false)
        return UserViewHolder(view, connectFriendButtonCallback)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun setData(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }
}