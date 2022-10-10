package com.sburov.aboutweather.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.sburov.aboutweather.data.mappers.toWeatherInfo
import com.sburov.aboutweather.domain.LocationProvider
import com.sburov.aboutweather.domain.LocationReceiver
import com.sburov.aboutweather.domain.WeatherDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationReceiver: LocationReceiver,
    private val locationProvider: LocationProvider,
    private val weatherDataProvider: WeatherDataProvider,
) : ViewModel() {

    var info by mutableStateOf<Either<DataError, WeatherInfo>>(Either.Left(DataError.GeolocationUnavailable))
        private set

    fun loadWeatherInfo() {
        val infoRef = info
        when {
            infoRef is Either.Left && infoRef.value is DataError.InProgress -> {
                return
            }
            else -> viewModelScope.launch {
                info = Either.Left(DataError.InProgress)

                info = locationProvider.getCurrentLocation()?.let { location ->
                    weatherDataProvider.getForecast(location)
                        .bimap({ _ -> DataError.WeatherInfoUnavailable }, { data -> data.toWeatherInfo() })
                } ?: Either.Left(DataError.GeolocationUnavailable)
            }
        }
    }

    fun receiveWeatherInfo() {
        val infoRef = info
        when {
            infoRef is Either.Left && infoRef.value is DataError.InProgress -> {
                return
            }
            else -> viewModelScope.launch {
                info = Either.Left(DataError.InProgress)
                locationReceiver.addLocationDataUpdateListener {
                    info = weatherDataProvider.getForecast(it)
                            .bimap({ _ -> DataError.WeatherInfoUnavailable }, { data -> data.toWeatherInfo() })
                }
                locationReceiver.startListeningToLocationUpdates()
            }
        }
    }
}
