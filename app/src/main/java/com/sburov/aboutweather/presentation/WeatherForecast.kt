package com.sburov.aboutweather.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Either

@Composable
fun WeatherForecast(
    info: Either<DataError, WeatherInfo>,
    modifier: Modifier = Modifier
) {
    when (info) {
        is Either.Left -> {}
        is Either.Right -> {
            info.value.forecast?.let { data ->
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Today",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(content = {
                        items(data) { weatherData ->
                            WeatherHourlyDisplay(
                                weatherData = weatherData,
                                modifier = Modifier
                                    .height(100.dp)
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    })
                }
            }
        }
    }
}
