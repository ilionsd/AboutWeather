package com.sburov.aboutweather.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

typealias LocationUpdateListener = (Location) -> Unit

@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
class LocationReceiver(
    private val context: Context
) {
    companion object {
        private const val TAG = "LocationReceiver"
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val listeners = mutableSetOf<LocationUpdateListener>()

    private var isListeningToLocationUpdates = false

    suspend fun getLastLocation(): Location? = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.runCatching { lastLocation }.getOrNull()?.apply {
            if (isComplete) {
                if (isSuccessful) {
                    continuation.resume(result)
                }
                else {
                    continuation.resume(null)
                }
                return@suspendCancellableCoroutine
            }
            addOnSuccessListener {
                continuation.resume(it)
            }
            addOnFailureListener {
                continuation.resume(null)
            }
            addOnCanceledListener {
                continuation.cancel()
            }
        }
    }
    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        val tokenSource = CancellationTokenSource()
        fusedLocationClient
            .runCatching { getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, tokenSource.token)  }
            .getOrNull()?.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        continuation.resume(result)
                    }
                    else {
                        continuation.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    continuation.resume(it)
                }
                addOnFailureListener {
                    continuation.resume(null)
                }
                addOnCanceledListener {
                    continuation.cancel()
                }
            }
    }

    val lastLocation: Task<Location>?
        get() = fusedLocationClient.runCatching {
            lastLocation
        }.getOrNull()

    val currentLocation: Task<Location>?
        get() = fusedLocationClient.runCatching {
            getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
        }.getOrNull()

    private fun sendLocationUpdate(location: Location?) {
        location?.let {
            for (listener in listeners) {
                listener(it)
            }
        }
    }

    private fun listeningToLocationUpdates(): Boolean {
        if (!isListeningToLocationUpdates) {
            isListeningToLocationUpdates = fusedLocationClient.runCatching {
                this.lastLocation.addOnSuccessListener(::sendLocationUpdate)
            }.isSuccess
        }
        return isListeningToLocationUpdates
    }

    fun addLocationUpdateListener(listener: LocationUpdateListener): Boolean =
        listeningToLocationUpdates() && listeners.add(listener)

    fun removeLocationUpdateListener(listener: LocationUpdateListener): Boolean = listeners.remove(listener)

}
