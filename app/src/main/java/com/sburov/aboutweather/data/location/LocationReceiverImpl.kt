package com.sburov.aboutweather.data.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.sburov.aboutweather.domain.LocationReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationReceiverImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient
): LocationReceiver {

    companion object {
        private const val TAG = "LocationReceiverImpl"
        private const val LOCATION_UPDATE_INTERVAL_10M = 10L * 60L * 1000L // 10m -> (* 60) 600s -> (* 1000) 600 000ms
        private const val LOCATION_UPDATE_DISPLACEMENT_1000M = 1000.0F
        private const val LOCATION_UPDATE_MAXWAITTIME_5S = 5L * 1000L
    }

    private val listeners = mutableSetOf<suspend (Location) -> Unit>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            lastLocationData = locationResult.lastLocation
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            isLocationDataAvailable = locationAvailability.isLocationAvailable
        }
    }

    override var isListeningToLocationUpdates: Boolean = false
        private set

    override var isLocationDataAvailable: Boolean = false
        private set(value) {
            if (field != value) {
                field = value
                onLocationDataAvailabilityChanged()
            }
        }

    override var lastLocationData: Location? = null
        private set(value) {
            field = value
            onLocationDataUpdated()
        }

    private fun onLocationDataAvailabilityChanged() {
        Log.d(TAG, "Location data availability changed to: $isLocationDataAvailable")
    }

    private fun onLocationDataUpdated() {
        Log.d(TAG, "Location data updated to: $lastLocationData")
        lastLocationData?.let {
            GlobalScope.launch {
                for (listener in listeners) {
                    listener(it)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun startListeningToLocationUpdates() {
        if (!isListeningToLocationUpdates) {
            locationClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, LOCATION_UPDATE_INTERVAL_10M)
                    .setMinUpdateDistanceMeters(LOCATION_UPDATE_DISPLACEMENT_1000M)
                    .setMaxUpdateDelayMillis(LOCATION_UPDATE_MAXWAITTIME_5S)
                    .setWaitForAccurateLocation(false)
                    .build(),
                locationCallback,
                Looper.getMainLooper()
            ).apply {
                addOnSuccessListener {
                    isListeningToLocationUpdates = true
                }
            }
        }
    }

    override fun stopListeningLocationUpdates() {
        if (isListeningToLocationUpdates) {
            locationClient.removeLocationUpdates(locationCallback).apply {
                addOnSuccessListener {
                    isListeningToLocationUpdates = false;
                    isLocationDataAvailable = false;
                }
            }
        }
    }

    override fun addLocationDataUpdateListener(listener: suspend (Location) -> Unit): Boolean = listeners.add(listener)

    override fun removeLocationDataUpdateListener(listener: suspend (Location) -> Unit): Boolean = listeners.remove(listener)

}
