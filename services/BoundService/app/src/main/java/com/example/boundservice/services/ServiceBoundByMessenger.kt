package com.example.boundservice.services

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log

import android.widget.Toast
import androidx.lifecycle.LifecycleService

class ServiceBoundByMessenger : LifecycleService() {

    companion object {
        private val className = ServiceBoundByMessenger::class.java.simpleName

        const val MESSAGE_KEY_SHOW_TOAST = 1
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        messenger = Messenger(MessageHandler(this))
        return messenger.binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ServiceBoundByMessenger", "onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ServiceBoundByMessenger", "onDestroy()")
    }

    private lateinit var messenger: Messenger

    class MessageHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            when (message.what) {
                MESSAGE_KEY_SHOW_TOAST -> Toast.makeText(
                    applicationContext,
                    className,
                    Toast.LENGTH_SHORT
                ).show()
                else -> super.handleMessage(message)
            }
        }
    }
}
