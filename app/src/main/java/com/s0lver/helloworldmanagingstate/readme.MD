# README for API

* Sample URL:

```
https://api.open-meteo.com/v1/forecast?latitude=37.3688&longitude=-122.0363&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=America%2FLos_Angeles&forecast_days=1
```

You will get responses like:

```
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
```
