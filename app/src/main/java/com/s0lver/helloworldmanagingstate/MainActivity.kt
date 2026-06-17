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
import java.util.Date

private const val API_URL =
    "https://api.open-meteo.com/v1/forecast?latitude=37.3688&longitude=-122.0363&daily=temperature_2m_max,temperature_2m_min&timezone=America%2FLos_Angeles&forecast_days=1&&current=weather_code,temperature_2m"

private const val FORECAST_API_URL =
    "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max,temperature_2m_min,weather_code&timezone=America%2FLos_Angeles&start_date=%DATE%&end_date=%DATE%"

// TODO: Fix this warning, there might be a better class for this nowadays
private val sdf = SimpleDateFormat("yyyy-MM-dd")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentWeather by remember { mutableStateOf<WeatherRecord?>(null) }
            var currentDate by remember { mutableStateOf(Date()) }

            HelloWorldManagingStateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        weatherRecord = currentWeather,
                        date = currentDate,
                        modifier = Modifier.padding(innerPadding),
                        onWeatherFetched = { record, date ->
                            currentWeather = record
                            currentDate = date
                        })
                }
            }
        }
    }
}

@Composable
fun Greeting(
    weatherRecord: WeatherRecord?,
    date: Date,
    modifier: Modifier = Modifier,
    onWeatherFetched: (WeatherRecord, Date) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column {
        // TODO: Confirm that date has a value, here this is being used without that check and using
        //  actually the check for the weather record instance.
        weatherRecord?.let {
            Text(
                text = "Weather for ${sdf.format(date)} will be ${it.weather} min: ${it.minTemperature}, max: ${it.maxTemperature}",
                modifier = modifier
            )
        } ?: Text(text = "Weather not fetched yet", modifier = modifier)

        Button(onClick = {
            // Checking out the network request approach
            scope.launch {
                // No need to wrap this in withContext(Dispatchers.IO) anymore!
                // The function handles its own threading.
                // TODO: Need to handle that day gets its successor after each click
                fetchWeatherData(context, date, onWeatherFetched)
            }
        }) { Text(text = "Fetch weather data?") }
    }
}

fun fetchWeatherData(context: Context, date: Date, onResult: (WeatherRecord, Date) -> Unit) {
    val queue = Volley.newRequestQueue(context)
    val preparedUrl = FORECAST_API_URL.replace("%DATE%", sdf.format(date))
    val jsonRequest = JsonObjectRequest(Request.Method.GET, preparedUrl, null, { response ->
        // Success: response is a JSONObject
        val dailyNode = response.getJSONObject("daily")
        val minTemperature = dailyNode.getJSONArray("temperature_2m_min").getDouble(0)
        val maxTemperature = dailyNode.getJSONArray("temperature_2m_max").getDouble(0)
        val weatherCode = dailyNode.getJSONArray("weather_code").getInt(0)
        val weather = Weather.fromCode(weatherCode)

        val newWeather = WeatherRecord(weather, minTemperature, maxTemperature)

        // Passing a different reference back in the event causes the Composable to see that a
        // new object is returned and forcing the UI update
        onResult(newWeather, date)
    }, { error ->
        // Error: handle the exception
        Log.e("Network", "That didn't work: ${error.message}")
    })

    queue.add(jsonRequest)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWorldManagingStateTheme {
        Greeting(weatherRecord = null, date = Date(), onWeatherFetched = { _, _ -> })
    }
}
