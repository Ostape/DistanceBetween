package com.robosh.distancebetween.locationscreen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.R
import com.robosh.distancebetween.databinding.FragmentLocationDistanceBinding
import com.robosh.distancebetween.locationservice.ForegroundLocationService


class LocationDistanceFragment : Fragment() {

    private lateinit var binding: FragmentLocationDistanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundLocationService()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
//                    val navOps = NavOptions.Builder()
//                        .setExitAnim(R.anim.slide_out_to_left)
//                        .setPopExitAnim(R.anim.slide_out_to_right)
//                        .build()
//                    findNavController().navigate(
//                        R.id.action_locationDistanceFragment_to_homeScreenFragment,
//                        null,
//                        navOps
//                    )
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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
        super.onDestroy()
        stopForegroundLocationService()
    }

    private fun startForegroundLocationService() {
        val serviceIntent = Intent(requireContext(), ForegroundLocationService::class.java)
        ContextCompat.startForegroundService(requireContext(), serviceIntent)
    }

    private fun stopForegroundLocationService() {
        val serviceIntent = Intent(requireContext(), ForegroundLocationService::class.java)
        requireContext().stopService(serviceIntent)
    }
}