package com.example.filecreationworker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    private lateinit var createFileLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        startFileCreationWorker()

        setContent {
            FileCreationApp(createFileLauncher = createFileLauncher)
        }
    }

    private fun startFileCreationWorker() {
        createFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    val inputData = workDataOf(
                        FileCreationWorker.KEY_OUTPUT_URI to it.toString()
                    )
                    // Enqueue the work request for processing
                    workRequestFun(inputData, applicationContext)
                }
            }
        }
    }

    private fun askNotificationPermission() {
        //https://developer.android.com/training/permissions/requesting
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                } else {
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
        }
    }
}

@Composable
fun FileCreationApp(createFileLauncher: ActivityResultLauncher<Intent>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "File Creation App", modifier = Modifier.padding(bottom = 16.dp))

        Button(onClick = {
            val pickerInitialUri = Uri.parse("content://path_to_directory")
            createFile(createFileLauncher, pickerInitialUri)
        }) {
            Text("Create PDF File")
        }
    }
}

//https://developer.android.com/training/data-storage/shared/documents-files
fun createFile(createFileLauncher: ActivityResultLauncher<Intent>, pickerInitialUri: Uri) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"
        putExtra(Intent.EXTRA_TITLE, "example.pdf")
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
    }
    createFileLauncher.launch(intent)
}

//WORK REQUEST
fun workRequestFun(inputData: Data, context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<FileCreationWorker>()
        .setInputData(inputData)
        .setInitialDelay(1, TimeUnit.SECONDS) // Optional, for delay
        .build()

    // Enqueue the WorkRequest
    WorkManager.getInstance(context).enqueue(workRequest)
}

