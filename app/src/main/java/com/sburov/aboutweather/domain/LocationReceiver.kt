package com.sburov.aboutweather.domain

import android.location.Location

interface LocationReceiver {
    val isListeningToLocationUpdates: Boolean
    val isLocationDataAvailable: Boolean
    val lastLocationData: Location?

    fun startListeningToLocationUpdates()
    fun stopListeningLocationUpdates()

    fun addLocationDataUpdateListener(listener: suspend (Location) -> Unit): Boolean
    fun removeLocationDataUpdateListener(listener: suspend (Location) -> Unit): Boolean
}