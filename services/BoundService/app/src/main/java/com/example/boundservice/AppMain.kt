package com.example.boundservice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun AppMain(
    binderServiceState: ServiceState,
    messengerServiceState: ServiceState,
    aidlServiceState: ServiceState
) {
    val services = listOf(
        Triple("Binder Service", binderServiceState,null),
        Triple("Messenger Service", messengerServiceState,null),
        Triple("AIDL Service", aidlServiceState,null)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        services.forEach { (serviceName, serviceState) ->
            ServiceControls(serviceName, serviceState)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}