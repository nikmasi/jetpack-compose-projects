package com.example.environmentsensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class EnvironmentSensor(context: Context) : SensorEventListener {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Declare sensors
    private var pressureSensor: Sensor? = null
    private var lightSensor: Sensor? = null
    private var humiditySensor: Sensor? = null
    private var ambientTemperatureSensor: Sensor? = null
    private var temperatureSensor: Sensor? = null

    // Mutable state for sensor data
    var pressure: MutableState<String> = mutableStateOf("N/A")
    var light: MutableState<String> = mutableStateOf("N/A")
    var humidity: MutableState<String> = mutableStateOf("N/A")
    var ambientTemperature: MutableState<String> = mutableStateOf("N/A")
    var temperature: MutableState<String> = mutableStateOf("N/A")

    init {
        // Initialize sensors
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE)

        if (pressureSensor == null) {
            pressure.value = "Senzor za pritisak nije dostupan na ovom uređaju."
        }
        if (lightSensor == null) {
            light.value = "Senzor za osvetljenost nije dostupan na ovom uređaju."
        }
        if (humiditySensor == null) {
            humidity.value = "Senzor za vlaznost okoline nije dostupan na ovom uređaju."
        }
        if (ambientTemperatureSensor == null) {
            ambientTemperature.value = "Senzor za temperaturu nije dostupan na ovom uređaju."
        }
        if (temperatureSensor == null) {
            temperature.value = "Senzor za temperaturu uređaju nije dostupan na ovom uređaju."
        }
    }

    // Start listening for sensor events
    fun startListening() {
        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        humiditySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        ambientTemperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        temperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    // Stop listening for sensor events
    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_PRESSURE -> {
                    pressure.value = "${event.values[0]} hPa"
                }
                Sensor.TYPE_LIGHT -> {
                    light.value = "${event.values[0]} lx"
                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    humidity.value = "${event.values[0]} %"
                }
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    ambientTemperature.value = "${event.values[0]} °C"
                }
                Sensor.TYPE_TEMPERATURE -> {
                    temperature.value = "${event.values[0]} °C"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle sensor accuracy changes if needed
    }
}