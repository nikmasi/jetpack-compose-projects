package com.example.foregroundservice


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForegroundService2 : Service(), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val serviceJob = SupervisorJob() // SupervisorJob za upravljanje korutinama
    private val coroutineScope = CoroutineScope(Dispatchers.IO + serviceJob)

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "foreground_service_channel"
        const val NOTIFICATION_ID = 2

        fun startService(context: Context) {
            Log.d("fore2","start")
            val intent = Intent(context, ForegroundService2::class.java)
            context.startService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, ForegroundService2::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("fore2","create")
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, getNotification())
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        Log.d("fore2","onsta")

        coroutineScope.launch {
            repeat(10) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Task iteration: $it", Toast.LENGTH_SHORT).show()
                }
                log("MyForegroundService", "Task iteration: $it")

                delay(3000)
            }
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        serviceJob.cancel() // Otkazivanje svih aktivnih korutina
        super.onDestroy()
    }


    private fun createNotificationChannel() {
        // https://developer.android.com/develop/ui/views/notifications/build-notification
        val notificationChannel = NotificationChannelCompat
            .Builder(NOTIFICATION_CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
            .setName("General channel")
            .build()
        NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel)
    }

    private fun getNotification(): Notification {
        // https://developer.android.com/develop/ui/views/notifications/build-notification
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle("My Foreground Service ")
            setContentText("Foreground Service with LifecycleRegistry and coroutineScope")
            setSmallIcon(R.drawable.baseline_notifications_24)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                foregroundServiceBehavior = Notification.FOREGROUND_SERVICE_IMMEDIATE
            }
        }
        return notificationBuilder.build()
    }

    private fun log(tag: String, message: String) {
        println("[$tag]: $message")
    }

    override fun onBind(intent: Intent?) = null

    override val lifecycle: Lifecycle = lifecycleRegistry
}

//https://stackoverflow.com/questions/14182014/android-oncreate-or-onstartcommand-for-starting-service
//https://stackoverflow.com/questions/16045722/android-notification-is-not-showing