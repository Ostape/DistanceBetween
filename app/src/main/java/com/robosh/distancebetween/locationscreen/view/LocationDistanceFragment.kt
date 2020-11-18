package com.robosh.distancebetween.locationscreen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.robosh.distancebetween.databinding.FragmentLocationDistanceBinding
import com.robosh.distancebetween.locationservice.ForegroundLocationService

class LocationDistanceFragment : Fragment() {

    private lateinit var binding: FragmentLocationDistanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundLocationService()
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