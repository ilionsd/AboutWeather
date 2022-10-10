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
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import arrow.core.Either
import com.sburov.aboutweather.R
import com.sburov.aboutweather.domain.LocationReceiver
import com.sburov.aboutweather.presentation.ui.theme.AboutWeatherTheme
import com.sburov.aboutweather.presentation.ui.theme.DarkBlue
import com.sburov.aboutweather.presentation.ui.theme.DeepBlue
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private val permissionLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.receiveWeatherInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkFineLocationPermission(this) ||
            checkCoarseLocationPermission(this)) {
            viewModel.receiveWeatherInfo()
        }
        else {
            requestPermissions()
        }

        setContent {
            AboutWeatherTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (val info = viewModel.info) {
                        is Either.Left -> when (info.value) {
                            DataError.GeolocationUnavailable -> {
                                Text(
                                    text = getString(R.string.data_error_geolocation_unavailable),
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            DataError.WeatherInfoUnavailable -> {
                                Text(
                                    text = getString(R.string.data_error_weatherinfo_unavailable),
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            DataError.InProgress -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        is Either.Right -> {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .background(DarkBlue)) {
                                WeatherCard(
                                    info = info,
                                    backgroundColor = DeepBlue
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                WeatherForecast(info = info)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun requestPermissions() {
        permissionLauncher.launch(
            arrayOf(
                // Can't launch access background location permission alongside other permissions,
                // because the platform will ignore such request.
                // Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
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

