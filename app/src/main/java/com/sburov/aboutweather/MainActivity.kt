package com.sburov.aboutweather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import arrow.core.maybe
import com.sburov.aboutweather.location.LocationReceiver

class MainActivity : AppCompatActivity() {

    private val locationService = LocationReceiver()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                locationService.currentLocation?.continueWith { task -> task.takeIf { it.isSuccessful }?.result }
            } else -> {
                // No location access granted.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkCoarseLocationPermission(applicationContext).not().maybe { requestPermissions() }
    }

    private fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        )
    }
}

inline fun checkCoarseLocationPermission(context: Context) =
    checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

inline fun checkBackgroundLocationPermission(context: Context) =
    checkPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)

inline fun checkPermission(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

