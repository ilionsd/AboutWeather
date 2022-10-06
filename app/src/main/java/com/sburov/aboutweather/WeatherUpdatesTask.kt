package com.sburov.aboutweather

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sburov.aboutweather.data.location.LocationReceiver


class WeatherUpdatesTask (
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val locationService = LocationReceiver(LocationServices.getFusedLocationProviderClient(context))

    override suspend fun doWork(): Result {
        locationService.getCurrentLocation()
        return Result.success()
    }

}