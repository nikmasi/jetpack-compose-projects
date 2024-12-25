package com.example.foregroundservice


import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForegroundService1 : LifecycleService() {

    //https://kotlinlang.org/docs/object-declarations.html#companion-objects
    companion object {
        private val className = ForegroundService1::class.java.simpleName

        const val NOTIFICATION_CHANNEL_ID = "my_notification_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        log(className, "onStartCommand()")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, getNotification())

        lifecycle.coroutineScope.launch {
            repeat(10) {
                // Ovo je samo za demonstraciju
                Toast.makeText(
                    this@ForegroundService1,
                    "$className: $it",
                    Toast.LENGTH_SHORT
                ).show()
                log(className, "Message number: $it")
                delay(3000)
            }
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        log(className, "onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        log(className, "onDestroy()")
        stopForeground(true)
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannelCompat
            .Builder(NOTIFICATION_CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
            .setName("My Notification Channel")
            .build()
        NotificationManagerCompat.from(this).createNotificationChannel(notificationChannel)
    }

    private fun getNotification(): Notification {

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("This is a foreground service with LifecycleService")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    foregroundServiceBehavior = Notification.FOREGROUND_SERVICE_IMMEDIATE
                }
            }
            .build()
    }

    private fun log(tag: String, message: String) {
        Log.d(tag, message)
    }
}
