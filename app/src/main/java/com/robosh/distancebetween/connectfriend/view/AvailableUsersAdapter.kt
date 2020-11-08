package com.robosh.distancebetween.connectfriend.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robosh.distancebetween.R
import com.robosh.distancebetween.databinding.AvailableUserViewHolderBinding
import com.robosh.distancebetween.model.User

class AvailableUsersAdapter : RecyclerView.Adapter<MyVH>() {

    private val users: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.available_user_view_holder, parent, false)
        return MyVH(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.bind(users[position])
    }

    fun setData(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }
}

class MyVH(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: AvailableUserViewHolderBinding = AvailableUserViewHolderBinding.bind(view)

    fun bind(user: User) {
        binding.userId.text = user.id
        binding.usernameTextView.text = user.username
    }
}