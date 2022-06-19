package com.sburov.aboutweather.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

typealias LocationUpdateListener = (Location) -> Unit

@SuppressLint("MissingPermission")
class LocationReceiver : Service() {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    private val listeners = mutableSetOf<LocationUpdateListener>()

    private var isListeningToLocationUpdates = false

    val lastLocation: Task<Location?>?
        get() = fusedLocationClient.runCatching {
            lastLocation
        }.getOrNull()

    val currentLocation: Task<Location?>?
        get() = fusedLocationClient.runCatching {
            getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
        }.getOrNull()

    private fun sendLocationUpdate(location: Location?) {
        location?.let {
            for (listener in listeners) {
                listener(it)
            }
        }
    }

    private fun listeningToLocationUpdates(): Boolean =
        isListeningToLocationUpdates || fusedLocationClient.runCatching {
            this.lastLocation.addOnSuccessListener(::sendLocationUpdate)
        }.isSuccess.also {
            isListeningToLocationUpdates = it
        }

    fun addLocationUpdateListener(listener: LocationUpdateListener): Boolean =
        listeningToLocationUpdates() && listeners.add(listener)

    fun removeLocationUpdateListener(listener: LocationUpdateListener): Boolean = listeners.remove(listener)

    override fun onBind(intent: Intent?): IBinder? = null
}
