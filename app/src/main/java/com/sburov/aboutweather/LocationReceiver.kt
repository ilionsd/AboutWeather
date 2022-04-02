package com.sburov.aboutweather

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
import java.util.concurrent.atomic.AtomicReference

typealias LocationUpdateListener = (Location?) -> Unit

@SuppressLint("MissingPermission")
class LocationReceiver : Service() {
    private val fusedLocationClient: FusedLocationProviderClient

    private val listeners = mutableSetOf<LocationUpdateListener>()

    private var isListeningToLocationUpdates = false

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    val lastLocation: Task<Location?>?
        get() = kotlin.runCatching {
            fusedLocationClient.lastLocation
        }.getOrNull()

    val currentLocation: Task<Location?>?
        get() = CancellationTokenSource().runCatching {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, this.token)
        }.getOrNull()

    fun sendLocationUpdate(location: Location?) {
        for (listener in listeners) {
            listener(location)
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

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}
