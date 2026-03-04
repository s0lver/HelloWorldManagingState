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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val temperature = 22
private val dateFormatter: SimpleDateFormat
    get() = SimpleDateFormat("MMM d", Locale.getDefault())
private val calendar = Calendar.getInstance()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentForecast by remember {
                mutableStateOf(
                    WeatherForecast(
                        Date(), Weather.Sunny,
                        temperature
                    )
                )
            }

            HelloWorldManagingStateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        currentForecast = currentForecast,
                        modifier = Modifier.padding(innerPadding),
                        onChange = {
                            currentForecast = it
                            Log.i(
                                "WeatherForecast",
                                "Tomorrow will be ${currentForecast.date} and ${currentForecast.weather}"
                            )
                        })
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
            text = "Today is ${dateFormatter.format(currentForecast.date)} and it is ${currentForecast.weather}",
            modifier = modifier
        )

        Button(onClick = {
            calendar.time = currentForecast.date
            calendar.add(Calendar.DAY_OF_MONTH, 1)

            val weather =
                Weather.entries[(currentForecast.weather.ordinal + 1) % Weather.entries.size]

            // calendar.time is creating a new Date object on the fly, weird huh
            val updatedForecast =
                currentForecast.copy(
                    date = calendar.time,
                    weather = weather,
                    temperature = temperature
                )

            // Passing a different reference back in the event causes the Composable to see that a
            // new object is returned and forcing the UI update
            onChange(updatedForecast)
        }) { Text(text = "How would tomorrow be?") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorldManagingStateTheme {
        Greeting(WeatherForecast(Date(), Weather.Cloudy, temperature), onChange = {})
    }
}

enum class Weather {
    Sunny, Cloudy, Rainy, Snowy
}

data class WeatherForecast(val date: Date, val weather: Weather, val temperature: Int)