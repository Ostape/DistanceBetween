package com.robosh.distancebetween.connectfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.INTENT_USER_FROM_CONNECT_TO_FRIEND
import com.robosh.distancebetween.connectfriend.viewmodel.ConnectToFriendViewModel
import com.robosh.distancebetween.databinding.FragmentConnectToFriendBinding
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ConnectToFriendFragment : Fragment(), ConnectFriendButtonCallback {

    private lateinit var binding: FragmentConnectToFriendBinding
    private lateinit var availableUsersAdapter: AvailableUsersAdapter
    private val viewModel: ConnectToFriendViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConnectToFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        viewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            if (it.connectedFriendId.isNotEmpty()) {
                val bundle = Bundle().apply {
                    putParcelable(INTENT_USER_FROM_CONNECT_TO_FRIEND, it)
                }
                findNavController().navigate(
                    R.id.action_connectToFriendFragment_to_locationDistanceFragment,
                    bundle
                )
            }
        })
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