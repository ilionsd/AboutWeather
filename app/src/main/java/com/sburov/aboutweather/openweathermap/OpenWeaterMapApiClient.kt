package com.sburov.aboutweather

import com.sburov.aboutweather.openweathermap.JsonWeatherApiService
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

class OpenWeaterMapApiClient(
    val httpClient: OkHttpClient?,
    val converter: Converter.Factory?,
) {
    companion object {
        const val BASE_ENDPOINT = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = "placeholder"
    }

    fun getCurrentWeather() = Retrofit.Builder()
        .baseUrl(BASE_ENDPOINT)
        .addConverterFactory(converter)
        .client(httpClient)
        .build()
        .create(JsonWeatherApiService::class.java)!!

}
