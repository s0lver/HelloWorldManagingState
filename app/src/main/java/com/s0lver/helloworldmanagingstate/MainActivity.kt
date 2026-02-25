package com.s0lver.helloworldmanagingstate

import android.os.Bundle
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
                        name = "Android",
                        currentForecast = currentForecast,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, currentForecast: WeatherForecast, modifier: Modifier = Modifier) {
    Column() {
        Button(onClick = {
            // TODO: produce state change and handle it...
        }) { Text(text = "Click me $name") }
    }
    Text(
        text = "Today is $currentForecast.day and it is $currentForecast.weather",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorldManagingStateTheme {
        Greeting("Android", WeatherForecast(1, Weather.Cloudy))
    }
}

enum class Weather {
    Sunny, Cloudy, Rainy, Snowy
}

class WeatherForecast(var day: Int, var weather: Weather)