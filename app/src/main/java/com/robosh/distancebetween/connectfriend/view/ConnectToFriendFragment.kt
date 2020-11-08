package com.robosh.distancebetween.connectfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.robosh.distancebetween.connectfriend.viewmodel.ConnectToFriendViewModel
import com.robosh.distancebetween.databinding.FragmentConnectToFriendBinding
import timber.log.Timber

class ConnectToFriendFragment : Fragment() {

    private lateinit var binding: FragmentConnectToFriendBinding

    private lateinit var viewModel: ConnectToFriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ConnectToFriendViewModel::class.java)
        binding = FragmentConnectToFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllAvailableUsers().observe(viewLifecycleOwner, Observer {
            Timber.d(it.toString())
        })
    }
}