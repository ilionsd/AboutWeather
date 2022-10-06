package com.sburov.aboutweather.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.sburov.aboutweather.domain.LocationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

typealias LocationUpdateListener = (Location) -> Unit

@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
class LocationReceiver @Inject constructor(
    private val locationClient: FusedLocationProviderClient
): LocationProvider {
    companion object {
        private const val TAG = "LocationReceiver"
    }

    private val listeners = mutableSetOf<LocationUpdateListener>()

    private var isListeningToLocationUpdates = false

    override suspend fun getLastLocation(): Location? = suspendCancellableCoroutine { continuation ->
        locationClient.runCatching { lastLocation }.getOrNull()?.apply {
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
    override suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        val tokenSource = CancellationTokenSource()
        locationClient
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
        get() = locationClient.runCatching {
            lastLocation
        }.getOrNull()

    val currentLocation: Task<Location>?
        get() = locationClient.runCatching {
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
            isListeningToLocationUpdates = locationClient.runCatching {
                this.lastLocation.addOnSuccessListener(::sendLocationUpdate)
            }.isSuccess
        }
        return isListeningToLocationUpdates
    }

    fun addLocationUpdateListener(listener: LocationUpdateListener): Boolean =
        listeningToLocationUpdates() && listeners.add(listener)

    fun removeLocationUpdateListener(listener: LocationUpdateListener): Boolean = listeners.remove(listener)

}
