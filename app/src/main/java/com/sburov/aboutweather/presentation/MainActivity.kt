package com.sburov.aboutweather.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.sburov.aboutweather.presentation.ui.theme.AboutWeatherTheme
import com.sburov.aboutweather.presentation.ui.theme.DarkBlue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private val permissionLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.loadWeatherInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkFineLocationPermission(this) ||
            checkCoarseLocationPermission(this)) {
            requestPermissions()
        }

        setContent {
            AboutWeatherTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBlue)) {

                    }

                }
            }
        }
    }

    private fun requestPermissions() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        )
    }
}

fun checkFineLocationPermission(context: Context) =
    checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

fun checkCoarseLocationPermission(context: Context) =
    checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

fun checkBackgroundLocationPermission(context: Context) =
    checkPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)

fun checkPermission(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

