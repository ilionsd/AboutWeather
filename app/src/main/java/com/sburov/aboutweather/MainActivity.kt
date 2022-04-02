package com.sburov.aboutweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
                // No location access granted.
            }
        }
    }

    val backgroundLocationPermissionRequest = when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

        else -> {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun requestPermissions() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION))
    }
}

inline fun checkCoarseLocationPermission(context: Context) =
    checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

@SuppressLint("InlinedApi")
inline fun checkBackgroundLocationPermission(context: Context) =
    (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            || checkPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)

inline fun checkPermission(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

