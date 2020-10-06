package com.robosh.distancebetween

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.robosh.distancebetween.locationservice.ForegroundLocationService
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private companion object {
        const val ACCESS_FINE_LOCATION_PERMISSION_CODE = 20
    }

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundLocationService? = null

    private var isReceivingLocationUpdates: Boolean = false

    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        isReceivingLocationUpdates =
            sharedPref.getBoolean(getString(R.string.is_receiving_location_updates), false)

        if (isReceivingLocationUpdates) {
            receiveLocationUpdatesBtn.text = getString(R.string.stop_receiving_location)
        }

        receiveLocationUpdatesBtn.setOnClickListener {
            if (isReceivingLocationUpdates) {
                onStopReceiveLocationUpdates()
            } else {
                checkForAccessLocationPermission()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
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
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
                onStartReceiveLocationUpdates()
            } else {
                Timber.d("Permission DENIED")
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkForAccessLocationPermission() {
        if (isPermissionGranted()) {
            onStartReceiveLocationUpdates()
            Timber.d("You have already have permission")
            Toast.makeText(this, "You have already have permission", Toast.LENGTH_SHORT).show()
        } else {
            requestAccessLocationPermission()
        }
    }

    private fun isPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestAccessLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // todo replace with DialogFragment
            AlertDialog.Builder(this)
                // todo extract to strings
                .setTitle("Permission needed Title")
                .setMessage("I need this permission Message")
                .setPositiveButton("Ok") { _, _ ->
                    Timber.d("Request permission from Dialog")
                    ActivityCompat.requestPermissions(
                        this,
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
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_FINE_LOCATION_PERMISSION_CODE
            )
        }
    }

    private fun onStopReceiveLocationUpdates() {
        foregroundOnlyLocationService?.onUnSubscribe()
        receiveLocationUpdatesBtn.text = getString(R.string.start_receiving_location)
        isReceivingLocationUpdates = false
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(
            getString(R.string.is_receiving_location_updates),
            isReceivingLocationUpdates
        ).apply()
    }

    private fun onStartReceiveLocationUpdates() {
        foregroundOnlyLocationService?.onSubscribe()
        receiveLocationUpdatesBtn.text = getString(R.string.stop_receiving_location)
        isReceivingLocationUpdates = true
        // todo extract to extensions
        val sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(
            getString(R.string.is_receiving_location_updates),
            isReceivingLocationUpdates
        ).apply()
    }
}