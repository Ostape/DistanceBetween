package com.robosh.distancebetween.homescreen.view

import android.Manifest
import android.content.ComponentName
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.robosh.distancebetween.application.INTENT_USERNAME
import com.robosh.distancebetween.databinding.FragmentHomeScreenBinding
import com.robosh.distancebetween.homescreen.viewmodel.HomeScreenViewModel
import com.robosh.distancebetween.locationservice.ForegroundLocationService
import timber.log.Timber


class HomeScreenFragment : Fragment() {

    private companion object {
        const val ACCESS_FINE_LOCATION_PERMISSION_CODE = 20
    }

    private lateinit var binding: FragmentHomeScreenBinding

    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(HomeScreenViewModel::class.java)
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(INTENT_USERNAME)
        binding.usernameTextView.text = username
        binding.connectToFriendButton.setOnClickListener {
            checkForAccessLocationPermission()
        }
        binding.waitForConnectionButton.setOnClickListener {
            checkForAccessLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                Timber.d("Permission GRANTED")
                showToast("Permission GRANTED")
                startForegroundLocationService()
            } else {
                Timber.d("Permission DENIED")
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
                intent.data = uri
                startActivityForResult(intent, ACCESS_FINE_LOCATION_PERMISSION_CODE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForegroundLocationService()
    }

    private fun checkForAccessLocationPermission() {
        val locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                startForegroundLocationService()
                Timber.d("You have already have permission")
                // todo extract to strings
                showToast("You have already have permission")
            } else {
                requestAccessLocationPermission()
            }
        } else {
            showToast("Enable GPS!!!")
        }
    }

    private fun requestAccessLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // todo replace with DialogFragment
            AlertDialog.Builder(requireContext())
                .setTitle("Permission needed Title")
                .setMessage("I need this permission Message")
                .setPositiveButton("Ok") { _, _ ->
                    Timber.d("Request permission from Dialog")
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        ACCESS_FINE_LOCATION_PERMISSION_CODE
                    )
                }
                .setNegativeButton("cancel") { dialog, _ ->
                    Timber.d("Cancel permission Dialog")
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            Timber.d("Request permission")
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_FINE_LOCATION_PERMISSION_CODE
            )
        }
    }

    private fun startForegroundLocationService() {
        val serviceIntent = Intent(requireContext(), ForegroundLocationService::class.java)
        ContextCompat.startForegroundService(requireContext(), serviceIntent)
    }

    private fun stopForegroundLocationService() {
        val serviceIntent = Intent(requireContext(), ForegroundLocationService::class.java)
        requireContext().stopService(serviceIntent)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}