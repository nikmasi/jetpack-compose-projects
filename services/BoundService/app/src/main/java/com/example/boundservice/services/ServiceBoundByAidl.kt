package com.example.boundservice.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.boundservice.aidl.IMyService

//https://developer.android.com/develop/background-work/services/aidl
class ServiceBoundByAidl : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("ServiceBoundByAidl","onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ServiceBoundByAidl","onDestroy()")
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private val binder = object : IMyService.Stub() {
        override fun showToast(message: String?) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}

//https://medium.com/@dugguRK/aidl-in-android-kotlin-2dde0eea4ae6


/*
android {
    buildFeatures.aidl = true
    ...
}
buildFeatures {
    ...
    aidl = true
}
 */