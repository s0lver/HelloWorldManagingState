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

private const val API_URL =
    "https://api.open-meteo.com/v1/forecast?latitude=37.3688&longitude=-122.0363&daily=temperature_2m_max,temperature_2m_min&timezone=America%2FLos_Angeles&forecast_days=1&&current=weather_code,temperature_2m"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentWeather by remember { mutableStateOf<WeatherRecord?>(null) }

            HelloWorldManagingStateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        weatherRecord = currentWeather,
                        modifier = Modifier.padding(innerPadding),
                        onWeatherFetched = { currentWeather = it })
                }
            }
        }
    }
}

@Composable
fun Greeting(
    weatherRecord: WeatherRecord?,
    modifier: Modifier = Modifier,
    onWeatherFetched: (WeatherRecord) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column {
        weatherRecord?.let {
            Text(
                text = "Weather is ${it.weather} currently ${it.temperature}, min: ${it.minTemperature}, max: ${it.maxTemperature}",
                modifier = modifier
            )
        } ?: Text(text = "Weather not fetched yet", modifier = modifier)

        Button(onClick = {
            // Checking out the network request approach
            scope.launch {
                // No need to wrap this in withContext(Dispatchers.IO) anymore!
                // The function handles its own threading.
                fetchWeatherData(context, onWeatherFetched)
            }
        }) { Text(text = "Fetch weather data?") }
    }
}

fun fetchWeatherData(context: Context, onResult: (WeatherRecord) -> Unit) {
    val queue = Volley.newRequestQueue(context)

    val jsonRequest = JsonObjectRequest(Request.Method.GET, API_URL, null, { response ->
        // Success: response is a JSONObject
        val dailyNode = response.getJSONObject("daily")
        val minTemperature = dailyNode.getJSONArray("temperature_2m_min").getDouble(0)
        val maxTemperature = dailyNode.getJSONArray("temperature_2m_max").getDouble(0)

        val currentNode = response.getJSONObject("current")
        val weatherCode = currentNode.getInt("weather_code")
        val currentTemperature = currentNode.getDouble("temperature_2m")

        val weather = Weather.fromCode(weatherCode)
        val newWeather =
            WeatherRecord(weather, currentTemperature, minTemperature, maxTemperature)

        // Passing a different reference back in the event causes the Composable to see that a
        // new object is returned and forcing the UI update
        onResult(newWeather)
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
        Greeting(null, onWeatherFetched = {})
    }
}
