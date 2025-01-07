package com.example.environmentsensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.environmentsensors.ui.theme.EnvironmentSensorsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnvironmentSensorsTheme {
                EnvApp()
            }
        }
    }
}

@Composable
fun EnvApp() {
    // Get the context
    val context = LocalContext.current

    val sensorManager = remember { EnvironmentSensor(context = context) }

    val pressure = sensorManager.pressure.value
    val light = sensorManager.light.value
    val humidity = sensorManager.humidity.value
    val ambientTemperature = sensorManager.ambientTemperature.value
    val temperature = sensorManager.temperature.value

    // Start listening for sensor events when the UI is first launched
    LaunchedEffect(Unit) {
        sensorManager.startListening()
    }

    // Stop listening when the UI is paused
    DisposableEffect(Unit) {
        onDispose {
            sensorManager.stopListening()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            // Title text
            Text(
                text = "Senzor",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Pressure
            SensorCard(
                label = "Pritisak",
                value = pressure,
                icon = Icons.Default.Favorite
            )

            // Light
            SensorCard(
                label = "Svetlost",
                value = light,
                icon = Icons.Default.BrightnessHigh
            )

            // Humidity
            SensorCard(
                label = "Vlažnost",
                value = humidity,
                icon = Icons.Default.Cloud
            )

            // Ambient Temperature
            SensorCard(
                label = "Ambijentalna temperatura",
                value = ambientTemperature,
                icon = Icons.Filled.AcUnit
            )

            // Temperature
            SensorCard(
                label = "Temperatura",
                value = temperature,
                icon = Icons.Outlined.AcUnit
            )
        }
    }
}
//https://stackoverflow.com/questions/75127384/how-can-i-access-all-material-icons-in-android-studio-through-icon-in-jetpack-co

@Composable
fun SensorCard(label: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}