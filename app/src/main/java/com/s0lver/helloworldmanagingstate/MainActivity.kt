package com.s0lver.helloworldmanagingstate

import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.s0lver.helloworldmanagingstate.ui.theme.HelloWorldManagingStateTheme
import kotlinx.coroutines.launch
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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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

            // Checking out the network request approach
            scope.launch {
                // No need to wrap this in withContext(Dispatchers.IO) anymore!
                // The function handles its own threading.
                val result = doNetworkCall(context)

                // You are back on the Main thread here automatically
                Log.i("Network", "Result: $result")
            }
        }) { Text(text = "How would tomorrow be?") }
    }
}

suspend fun doNetworkCall(context: Context) {
    val queue = Volley.newRequestQueue(context)
    val url =
        "https://api.open-meteo.com/v1/forecast?latitude=37.3688&longitude=-122.0363&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=America%2FLos_Angeles&forecast_days=1"

    val jsonRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            // Success: response is a JSONObject
            val elevation = response.getString("elevation")
            println("Elevation is $elevation")
        },
        { error ->
            // Error: handle the exception
            println("That didn't work: ${error.message}")
        })

    queue.add(jsonRequest)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorldManagingStateTheme {
        Greeting(WeatherForecast(Date(), Weather.Cloudy, temperature), onChange = {})
    }
}

/*
TODO: Use this free API for weather forecast:
https://api.open-meteo.com/v1/forecast?latitude=37.3688&longitude=-122.0363&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=America%2FLos_Angeles&forecast_days=1
You will get responses like:
{
  "latitude": 37.371273,
  "longitude": -122.02964,
  "generationtime_ms": 0.125408172607422,
  "utc_offset_seconds": -25200,
  "timezone": "America/Los_Angeles",
  "timezone_abbreviation": "GMT-7",
  "elevation": 39,
  "daily_units": {
    "time": "iso8601",
    "weather_code": "wmo code",
    "temperature_2m_max": "°C",
    "temperature_2m_min": "°C"
  },
  "daily": {
    "time": [
      "2026-03-15"
    ],
    "weather_code": [45],
    "temperature_2m_max": [27.2],
    "temperature_2m_min": [9.5]
  }
}

Need to adjust your classes to accommodate these new structures.
Make the requests via retrofit/volley
Later on you can use gps to locate the device and use that info to personalize the request.
 */
