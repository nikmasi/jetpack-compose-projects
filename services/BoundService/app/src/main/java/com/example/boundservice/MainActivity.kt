package com.example.boundservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.boundservice.aidl.IMyService
import com.example.boundservice.services.ServiceBoundByAidl
import com.example.boundservice.services.ServiceBoundByBinder
import com.example.boundservice.services.ServiceBoundByMessenger
import com.example.boundservice.ui.theme.BoundServiceTheme

class MainActivity : ComponentActivity() {

    // Binder Service
    private lateinit var mService: ServiceBoundByBinder
    private var mBound: Boolean by mutableStateOf(false)

    private val connection = createServiceConnection(
        onServiceConnected = { service ->
            val binder = service as ServiceBoundByBinder.LocalBinder
            mService = binder.getService()
            mBound = true
        },
        onServiceDisconnected = { mBound = false }
    )

    // Messenger Service
    private lateinit var messengerForService: Messenger
    private var isBoundedService by mutableStateOf(false)

    private val connectionToService = createServiceConnection(
        onServiceConnected = { service ->
            messengerForService = Messenger(service)
            isBoundedService = true
        },
        onServiceDisconnected = { isBoundedService = false }
    )

    // AIDL Service
    private var isServiceBoundAidl by mutableStateOf(false)
    private lateinit var myServiceAidl: IMyService

    private val serviceConnectionAidl = createServiceConnection(
        onServiceConnected = { service ->
            myServiceAidl = IMyService.Stub.asInterface(service)
            isServiceBoundAidl = true
        },
        onServiceDisconnected = { isServiceBoundAidl = false }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoundServiceTheme {
                AppMain(
                    binderServiceState = createServiceState(
                        isBound = mBound,
                        onConnect = { bindToService(ServiceBoundByBinder::class.java, connection) },
                        onDisconnect = { unbindFromService(connection) },
                        onAction = { mService.showToast() }
                    ),
                    messengerServiceState = createServiceState(
                        isBound = isBoundedService,
                        onConnect = { bindToService(ServiceBoundByMessenger::class.java, connectionToService) },
                        onDisconnect = { unbindFromService(connectionToService) },
                        onAction = { sendMessageToService() }
                    ),
                    aidlServiceState = createServiceState(
                        isBound = isServiceBoundAidl,
                        onConnect = { bindToService(ServiceBoundByAidl::class.java, serviceConnectionAidl) },
                        onDisconnect = { unbindFromService(serviceConnectionAidl) },
                        onAction = { myServiceAidl.showToast("ServiceBoundByAidl") }
                    )
                )
            }
        }
    }

    private fun bindToService(serviceClass: Class<*>, connection: ServiceConnection) {
        Intent(this, serviceClass).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }
    var isServiceConnected by  mutableStateOf(false)
    private fun unbindFromService(connection: ServiceConnection) {
        try {
            when (connection) {
                this.connection -> {
                    if (mBound) {
                        unbindService(connection)
                        mBound = false
                        isServiceConnected = false // Ažurirajte stanje
                    }
                }
                this.connectionToService -> {
                    if (isBoundedService) {
                        unbindService(connection)
                        isBoundedService = false
                        isServiceConnected = false
                    }
                }
                this.serviceConnectionAidl -> {
                    if (isServiceBoundAidl) {
                        unbindService(connection)
                        isServiceBoundAidl = false
                        isServiceConnected = false
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }


    private fun sendMessageToService() {
        val message = Message.obtain(null, ServiceBoundByMessenger.MESSAGE_KEY_SHOW_TOAST)
        try {
            messengerForService.send(message)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun createServiceConnection(
        onServiceConnected: (IBinder) -> Unit,
        onServiceDisconnected: () -> Unit
    ) = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            onServiceConnected(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            onServiceDisconnected()
        }
    }

    @Composable
    private fun createServiceState(
        isBound: Boolean,
        onConnect: () -> Unit,
        onDisconnect: () -> Unit,
        onAction: () -> Unit
    ) = ServiceState(isBound, onConnect, onDisconnect, onAction)
}

//https://stackoverflow.com/questions/43947499/why-to-use-android-bound-service
//https://medium.com/@dugguRK/aidl-in-android-kotlin-2dde0eea4ae6