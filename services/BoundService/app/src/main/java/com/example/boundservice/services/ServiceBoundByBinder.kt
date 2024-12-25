package com.example.boundservice.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleService

//https://developer.android.com/develop/background-work/services/bound-services#Binder
class ServiceBoundByBinder: LifecycleService() {

    private val binder = LocalBinder()

    companion object{
        private val className = ServiceBoundByBinder::class.java.simpleName
    }

    inner class LocalBinder(): Binder(){
        fun getService(): ServiceBoundByBinder = this@ServiceBoundByBinder
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    fun showToast(){
        Toast.makeText(
            this,
            "ServiceBoundByBinder",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ServiceBoundByBinder","onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ServiceBoundByBinder","onDestroy()")
    }
}