package com.example.boundservice

data class ServiceState(
    val isBound: Boolean,
    val onConnect: () -> Unit,
    val onDisconnect: () -> Unit,
    val onAction: () -> Unit
)
