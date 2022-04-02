package com.sburov.aboutweather.openweathermap

import retrofit2.http.GET
import retrofit2.http.Query

interface JsonWeatherApiService {

    @GET("/weather")
    fun getCurrentWeather(
        @Query("appid") appID: String,
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: Units
    )
}