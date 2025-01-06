package com.example.simpletaskreminderworker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class TaskReminderWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams){
    private val CHANNEL_NAME = "task_reminder_channel"
    private val CHANNEL_DESC = "task_reminder_desc"
    private val CHANNEL_ID = "task_reminder_id"

    override suspend fun doWork(): Result {
        // dobavljanje podataka o zadatku
        val taskTitle = inputData.getString("task_title") ?: "No Title"
        val taskDescription = inputData.getString("task_description") ?: "No Description"

        // kreiranje kanala za notifikacije
        createNotificationChannel()

        // slanje notifikacije
        sendNotification(taskTitle, taskDescription)

        return Result.success()
    }

    private fun createNotificationChannel() {
        // kreiranje NotificationChannel-a samo za API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESC
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // registracija kanala sa sistemom
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                //!!
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(taskTitle: String, taskDescription: String) {
        // kreiranje notifikacije
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //iz dokumentacije
        var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(taskTitle)
            .setContentText(taskDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder
            .build()

        // slanje notifikacije
        notificationManager.notify(1, notification)
    }
}

