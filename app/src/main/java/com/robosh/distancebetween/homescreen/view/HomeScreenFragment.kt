package com.robosh.distancebetween.homescreen.view

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.ACCESS_FINE_LOCATION_PERMISSION_CODE
import com.robosh.distancebetween.application.INTENT_USERNAME
import com.robosh.distancebetween.databinding.FragmentHomeScreenBinding
import com.robosh.distancebetween.homescreen.viewmodel.HomeScreenViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private val homeScreenViewModel: HomeScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeScreenViewModel.setIsPermissionGranted(isLocationPermissionGranted())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(INTENT_USERNAME)
        binding.usernameTextView.text = username
        initNavigationButtons()
        initRequestLocationButtonListener()
        showButtonsIfPermissionsGranted()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                showToast("Permission GRANTED")
                homeScreenViewModel.setIsPermissionGranted(true)
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
                intent.data = uri
                startActivityForResult(intent, ACCESS_FINE_LOCATION_PERMISSION_CODE)
            }
        }
    }

    private fun checkForAccessLocationPermission() {
        if (isLocationPermissionGranted()) {
            showToast("You have already have permission")
        } else {
            requestAccessLocationPermission()
        }
    }

    private fun requestAccessLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission needed Title")
                .setMessage("I need this permission Message")
                .setPositiveButton("Ok") { _, _ ->
                    requestListOfPermissions()
                }
                .setNegativeButton("cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            requestListOfPermissions()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestListOfPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            ACCESS_FINE_LOCATION_PERMISSION_CODE
        )
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun initRequestLocationButtonListener() {
        binding.requestPermissionButton.setOnClickListener {
            checkForAccessLocationPermission()
        }
    }

    private fun initNavigationButtons() {
        binding.connectToFriendButton.setOnClickListener {
            if (isEnabledGps()) {
                findNavController().navigate(R.id.action_homeScreenFragment_to_connectToFriendFragment)
            }
        }
        binding.waitForConnectionButton.setOnClickListener {
            if (isEnabledGps()) {
                findNavController().navigate(R.id.action_homeScreenFragment_to_waitForFriendFragment)
            }
        }
    }

    private fun isEnabledGps(): Boolean {
        val locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        return if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            true
        } else {
            showToast("Enable GPS!!!")
            false
        }
    }

    private fun showButtonsIfPermissionsGranted() {
        homeScreenViewModel.isPermissionGranted.observe(viewLifecycleOwner, Observer { isGranted ->
            with(binding) {
                if (isGranted) {
                    waitForConnectionButton.visibility = VISIBLE
                    connectToFriendButton.visibility = VISIBLE
                    requestPermissionButton.visibility = GONE
                } else {
                    waitForConnectionButton.visibility = GONE
                    connectToFriendButton.visibility = GONE
                    requestPermissionButton.visibility = VISIBLE
                }
            }
        })
    }
}