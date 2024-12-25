package com.example.foregroundservice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foregroundservice.ui.theme.ForegroundServiceTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //https://developer.android.com/training/permissions/requesting#request-permission
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {

                } else {
                }
            }

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

        enableEdgeToEdge()
        setContent {
            ForegroundServiceTheme {
                MainScreen()
            }
        }
    }

    //Android API nivo 26 (Oreo) ili noviji
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MainScreen() {
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // lightgray
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Text(
                    text = "Foreground Service Control",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )


                Button(onClick = { startForegroundService(context) },
                    colors = ButtonDefaults.buttonColors(  Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(42.dp)
                ) {
                    Text(text = "Start Service 1", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }


                Button(onClick = { stopForegroundService(context) },
                    colors = ButtonDefaults.buttonColors(  Color(0xFFF44336)),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(42.dp)
                ) {
                    Text(text = "Stop Service 1", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = { ForegroundService2.startService(context) },
                    colors = ButtonDefaults.buttonColors(  Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(42.dp)
                ) {
                    Text(text = "Start Service 2", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }


                Button(onClick = { ForegroundService2.stopService(context) },
                    colors = ButtonDefaults.buttonColors(  Color(0xFFF44336)),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(42.dp)
                ) {
                    Text(text = "Stop Service 2", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }



    //https://developer.android.com/develop/background-work/services/fgs/launch
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService(context: Context) {
        val intent = Intent(context, ForegroundService1::class.java)
        context.startForegroundService(intent)
    }

    private fun stopForegroundService(context: Context) {
        val intent = Intent(context, ForegroundService1::class.java)
        context.stopService(intent)
    }
}

