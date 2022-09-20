package com.sburov.aboutweather.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.sburov.aboutweather.data.mappers.toWeatherInfo
import com.sburov.aboutweather.domain.LocationProvider
import com.sburov.aboutweather.domain.WeatherDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val weatherDataProvider: WeatherDataProvider,
) : ViewModel() {

    var info by mutableStateOf<Either<DataError, WeatherInfo>>(Either.Left(DataError.Unavailable))
        private set

    fun loadWeatherInfo() {
        val infoRef = info
        if (infoRef is Either.Left && infoRef.value is DataError.InProgress) {
            return
        }
        viewModelScope.launch {
            info = Either.Left(DataError.InProgress)
            locationProvider.getLastLocation()?.let { location ->
                info = weatherDataProvider.getForecast(location)
                    .bimap({ _ -> DataError.Unavailable }, { data -> data.toWeatherInfo() })
            } ?: kotlin.run {
                info = Either.Left(DataError.Unavailable)
            }
        }
    }
}
