package com.s0lver.helloworldmanagingstate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.s0lver.helloworldmanagingstate.ui.theme.HelloWorldManagingStateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentForecast by remember { mutableStateOf(WeatherForecast(1, Weather.Sunny)) }

            HelloWorldManagingStateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        currentForecast = currentForecast,
                        modifier = Modifier.padding(innerPadding),
                        onChange = {
                            currentForecast = it
                            Log.i(
                                "WeatherForecast",
                                "Tomorrow will be ${currentForecast.day} and ${currentForecast.weather}"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    currentForecast: WeatherForecast,
    modifier: Modifier = Modifier,
    onChange: (WeatherForecast) -> Unit
) {
    Column {
        Text(
            text = "Today is ${currentForecast.day} and it is ${currentForecast.weather}",
            modifier = modifier
        )

        Button(onClick = {
            var day = currentForecast.day + 1
            if (day > 31) {
                day = 1
            }

            val weather =
                Weather.entries[(currentForecast.weather.ordinal + 1) % Weather.entries.size]
            currentForecast.day = day
            currentForecast.weather = weather
            // TODO: fix the state hoisting issue
            onChange(currentForecast)
        }) { Text(text = "How would tomorrow be?") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorldManagingStateTheme {
        Greeting(WeatherForecast(1, Weather.Cloudy), onChange = {})
    }
}

enum class Weather {
    Sunny, Cloudy, Rainy, Snowy
}

class WeatherForecast(var day: Int, var weather: Weather)