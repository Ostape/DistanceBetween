package com.robosh.distancebetween.locationscreen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.ACTION_START_OR_RESUME_SERVICE
import com.robosh.distancebetween.application.ACTION_STOP_SERVICE
import com.robosh.distancebetween.application.INTENT_USER_FOR_SERVICE
import com.robosh.distancebetween.application.getDistanceFromLatLon
import com.robosh.distancebetween.database.RealtimeDatabaseImpl.Companion.CONNECTED_USER_INDEX
import com.robosh.distancebetween.database.RealtimeDatabaseImpl.Companion.CURRENT_USER_INDEX
import com.robosh.distancebetween.databinding.FragmentLocationDistanceBinding
import com.robosh.distancebetween.locationscreen.viewmodel.LocationDistanceViewModel
import com.robosh.distancebetween.locationservice.ForegroundLocationService
import com.robosh.distancebetween.model.User
import org.koin.android.viewmodel.ext.android.viewModel

class LocationDistanceFragment : Fragment() {

    private val locationDistanceViewModel: LocationDistanceViewModel by viewModel()
    private lateinit var binding: FragmentLocationDistanceBinding
    private var cachedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBackPressNavigation()
        sendCommandService(ACTION_START_OR_RESUME_SERVICE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDistanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLocationChanges()
        initButtonClickListener()
        locationDistanceViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            cachedUser = it
            if (it.connectedFriendId.isEmpty()) {
                navigateToHomeFragmentAndStopReceiveLocation()
            }
        })
    }

    private fun initButtonClickListener() {
        binding.stopSharingLocationButton.setOnClickListener {
            locationDistanceViewModel.stopShareLocation(cachedUser)
            navigateToHomeFragmentAndStopReceiveLocation()
        }
    }

    private fun initBackPressNavigation() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    locationDistanceViewModel.stopShareLocation(cachedUser)
                    navigateToHomeFragmentAndStopReceiveLocation()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun navigateToHomeFragmentAndStopReceiveLocation() {
        if (findNavController().popBackStack(R.id.homeScreenFragment, false).not()) {
            findNavController().navigate(R.id.action_locationDistanceFragment_to_homeScreenFragment)
        }
        sendCommandService(ACTION_STOP_SERVICE)
    }

    private fun observeLocationChanges() {
        locationDistanceViewModel.listenUsersChanges()
            .observe(viewLifecycleOwner, Observer { users ->
                if (users.size > 1) {
                    setViewData(users)
                }
            })
    }

    private fun setViewData(users: List<User>) {
        val currentUser = users[CURRENT_USER_INDEX]
        val connectedUser = users[CONNECTED_USER_INDEX]
        with(binding) {
            myLocationLatitude.text = currentUser.location?.latitude.toString()
            myLocationLongitude.text = currentUser.location?.longitude.toString()
            myFriendsLocationLatitude.text = connectedUser.location?.latitude.toString()
            myFriendsLocationLongitude.text = connectedUser.location?.longitude.toString()
            distanceBetween.text =
                getDistanceFromLatLon(currentUser.location, connectedUser.location)
        }
    }

    private fun sendCommandService(action: String) {
        Intent(requireContext(), ForegroundLocationService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }
}