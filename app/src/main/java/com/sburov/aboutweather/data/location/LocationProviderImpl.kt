package com.sburov.aboutweather.data.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.sburov.aboutweather.domain.LocationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class LocationProviderImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
): LocationProvider {

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): Location? = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.run {
            lastLocation
        }.apply {
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

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.run {
            getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        }.apply {
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

}
