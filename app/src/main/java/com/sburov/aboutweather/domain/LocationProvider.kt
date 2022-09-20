package com.sburov.aboutweather.domain

import android.location.Location

interface LocationProvider {
    suspend fun getLastLocation(): Location?
    suspend fun getCurrentLocation(): Location?
}