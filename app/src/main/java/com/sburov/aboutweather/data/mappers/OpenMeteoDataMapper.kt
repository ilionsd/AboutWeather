package com.sburov.aboutweather.data.mappers

import com.sburov.aboutweather.data.remote.openmeteo.CurrentWeather
import com.sburov.aboutweather.data.remote.openmeteo.OpenMeteoData
import com.sburov.aboutweather.data.remote.openmeteo.Variable
import com.sburov.aboutweather.data.serialization.DataGrid
import com.sburov.aboutweather.domain.weather.*
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.LocalDateTime

inline fun <reified T> toDisplayData(value: Any?, unit: String?): DisplayData<T>? = value ?. let {
    DisplayData(it as T, unit!!)
}

fun mapToDisplay(dataGrid: DataGrid<Variable>, units: Map<Variable, String>) : List<DisplayWeather> {
    val list = mutableListOf<DisplayWeather>()
    for (k in 0 until dataGrid.size) {
        try {
            DisplayWeather(
                // Main data
                time = dataGrid.get<LocalDateTime>(Variable.TIME, k)!!.toJavaLocalDateTime(),
                weatherType = dataGrid.get<Int>(Variable.WEATHER_CODE_WMO, k)!!.let { code ->
                    WeatherType.fromWMO(code)
                },
                temperature = toDisplayData(dataGrid[Variable.TEMPERATURE_2M, k], units[Variable.TEMPERATURE_2M])!!,
                // Included in current weather
                windSpeed = toDisplayData(dataGrid[Variable.WIND_SPEED_10M, k], units[Variable.WIND_SPEED_10M]),
                windDirection = toDisplayData(dataGrid[Variable.WIND_DIRECTION_10M, k], units[Variable.WIND_DIRECTION_10M]),
                // Forecast
                pressure = toDisplayData(dataGrid[Variable.PRESSURE_SURFACE, k], units[Variable.PRESSURE_SURFACE]),
                humidity = toDisplayData(dataGrid[Variable.HUMIDITY_RELATIVE_2M, k], units[Variable.HUMIDITY_RELATIVE_2M])
            )
        }
        catch (e: NullPointerException) {
            null
        } ?.let {
            list.add(it)
        }
    }
    return list
}

fun CurrentWeather.toDisplayWeather(units: Map<Variable, String>): DisplayWeather = DisplayWeather(
    time = time.toJavaLocalDateTime(),
    weatherType = weatherCodeWMO.let { WeatherType.fromWMO(it.toInt()) },
    temperature = DisplayData(temperature, units[Variable.TEMPERATURE_2M]!!),
    windSpeed = DisplayData(windSpeed, units[Variable.WIND_SPEED_10M]!!),
    windDirection = DisplayData(windDirection, units[Variable.WIND_DIRECTION_10M]!!),
    pressure = null,
    humidity = null,
)

fun OpenMeteoData.toDisplayData(): DisplayInfo {
    val current = currentWeather ?. toDisplayWeather(hourlyUnits!!)
    val forecast = hourlyData ?. let { dataGrid -> mapToDisplay(dataGrid, hourlyUnits!!) }
    return DisplayInfo(current, forecast)
}
