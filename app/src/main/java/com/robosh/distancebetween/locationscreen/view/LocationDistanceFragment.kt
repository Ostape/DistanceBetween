package com.robosh.distancebetween.locationscreen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.robosh.distancebetween.application.*
import com.robosh.distancebetween.databinding.FragmentLocationDistanceBinding
import com.robosh.distancebetween.locationservice.ForegroundLocationService
import com.robosh.distancebetween.model.User


class LocationDistanceFragment : Fragment() {

    private lateinit var binding: FragmentLocationDistanceBinding
    private var cachedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<User>(INTENT_USER_FROM_CONNECT_TO_FRIEND)?.let {
            cachedUser = it
        }
        arguments?.getParcelable<User>(INTENT_USER_FROM_WAIT_FRIEND)?.let {
            cachedUser = it
        }

//        val callback: OnBackPressedCallback =
//            object : OnBackPressedCallback(true /* enabled by default */) {
//                override fun handleOnBackPressed() {
////                    val navOps = NavOptions.Builder()
////                        .setExitAnim(R.anim.slide_out_to_left)
////                        .setPopExitAnim(R.anim.slide_out_to_right)
////                        .build()
////                    findNavController().navigate(
////                        R.id.action_locationDistanceFragment_to_homeScreenFragment,
////                        null,
////                        navOps
////                    )
//                }
//            }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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

    override fun onDestroy() {
        sendCommandService(ACTION_STOP_SERVICE)
        super.onDestroy()
    }

    private fun sendCommandService(action: String) {
        Intent(requireContext(), ForegroundLocationService::class.java).also {
            it.action = action
            it.putExtra(INTENT_USER_FOR_SERVICE, cachedUser)
            requireContext().startService(it)
        }
    }
}