package com.robosh.distancebetween.connectfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosh.distancebetween.connectfriend.viewmodel.ConnectToFriendViewModel
import com.robosh.distancebetween.databinding.FragmentConnectToFriendBinding
import timber.log.Timber

class ConnectToFriendFragment : Fragment(), ConnectFriendButtonCallback {

    private lateinit var binding: FragmentConnectToFriendBinding

    private lateinit var viewModel: ConnectToFriendViewModel

    private lateinit var availableUsersAdapter: AvailableUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ConnectToFriendViewModel::class.java)
        binding = FragmentConnectToFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.availableUsersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        availableUsersAdapter = AvailableUsersAdapter(this)

        viewModel.getAllAvailableUsers().observe(viewLifecycleOwner, Observer {
            availableUsersAdapter.setData(it)
            Timber.d(it.toString())
        })
        binding.availableUsersRecyclerView.adapter = availableUsersAdapter
    }

    override fun onConnectFriend(id: String?) {
        viewModel.pairConnectedUser(id)
    }
}