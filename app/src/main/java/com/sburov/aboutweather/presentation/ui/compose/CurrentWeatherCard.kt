package com.sburov.aboutweather.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Either
import com.sburov.aboutweather.R
import com.sburov.aboutweather.domain.weather.DataError
import com.sburov.aboutweather.domain.weather.DisplayInfo
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun WeatherCard(
    info: Either<DataError, DisplayInfo>,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    when (info) {
        is Either.Left -> {}
        is Either.Right -> {
            info.value.current?.let { data ->
                Card(
                    backgroundColor = backgroundColor,
                    shape = RoundedCornerShape(10.dp),
                    modifier = modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Today ${
                                data.time.format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                )
                            }",
                            modifier = Modifier.align(Alignment.End),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = data.weatherType.iconRes),
                            contentDescription = null,
                            modifier = Modifier.width(200.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${data.temperature}",
                            fontSize = 50.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = data.weatherType.weatherDesc,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            data.pressure ?. let {
                                WeatherDataDisplay(
                                    text = "$it",
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
                                    iconTint = Color.White,
                                    textStyle = TextStyle(color = Color.White)
                                )
                            }
                            data.pressure ?. let {
                                WeatherDataDisplay(
                                    text = "$it",
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                                    iconTint = Color.White,
                                    textStyle = TextStyle(color = Color.White)
                                )
                            }
                            data.windSpeed ?. let {
                                WeatherDataDisplay(
                                    text = "$it",
                                    icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
                                    iconTint = Color.White,
                                    textStyle = TextStyle(color = Color.White)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}