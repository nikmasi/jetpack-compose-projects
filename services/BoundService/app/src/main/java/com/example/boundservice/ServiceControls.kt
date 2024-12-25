package com.example.boundservice

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ServiceControls(serviceName: String, state: ServiceState) {
    Text(
        text = if (state.isBound) "$serviceName is connected!" else "$serviceName is not connected.",
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        onClick = state.onConnect,
        colors = ButtonDefaults.buttonColors(Color(0xFF6200E1)),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text("Connect", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White))
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        onClick = state.onAction,
        enabled = state.isBound,
        colors = ButtonDefaults.buttonColors(Color(0xFF6200E1)),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text("Perform Action", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White))
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        onClick = state.onDisconnect,
        enabled = state.isBound,
        colors = ButtonDefaults.buttonColors(Color.Red),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text("Disconnect", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White))
    }
}