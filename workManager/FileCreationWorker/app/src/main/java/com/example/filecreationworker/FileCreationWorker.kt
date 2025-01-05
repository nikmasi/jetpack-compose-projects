package com.example.filecreationworker

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.work.*
import java.io.OutputStream

class FileCreationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val outputUri: Uri = Uri.parse(inputData.getString(KEY_OUTPUT_URI))

        return try {
            createPdf(outputUri)

            sendNotification()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun createPdf(outputUri: Uri) {
        val contentResolver = applicationContext.contentResolver
        val outputStream: OutputStream = contentResolver.openOutputStream(outputUri)!!

        val document = PdfDocument()

        // adding page
        val pageInfo = PdfDocument.PageInfo.Builder(795, 920, 1).create() // A4 stranica
        val page = document.startPage(pageInfo)

        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 36f

        val canvas: Canvas = page.canvas
        canvas.drawText("This is a sample PDF file content.", 100f, 100f, paint)
        document.finishPage(page)
        document.writeTo(outputStream)
        document.close()
    }

    @SuppressLint("NotificationPermission", "NewApi")
    private fun sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "file_creation_channel"
            val channelName = "File Creation Notifications"
            val channelDescription = "Notifications for file creation progress."

            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = Notification.Builder(applicationContext, "file_creation_channel")
            .setContentTitle("File Created")
            .setContentText("Your file has been created successfully!")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    companion object {
        const val KEY_OUTPUT_URI = "output_uri"
    }
}
