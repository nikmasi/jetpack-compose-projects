package com.example.simpletaskreminderworker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.simpletaskreminderworker.ui.theme.SimpleTaskReminderWorkerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        enableEdgeToEdge()
        setContent {
            SimpleTaskReminderWorkerTheme {
                TimePickerExamples()
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

    fun scheduleTaskReminder(time: Calendar, title: String, description: String) {
        val triggerTimeMillis = time.timeInMillis

        // zakazi TaskReminderWorker
        val workRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
            .setInitialDelay(triggerTimeMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(
                "task_title" to title,
                "task_description" to description
            ))
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerExamples() {
    var showMenu by remember { mutableStateOf(true) }
    var showAdvancedExample by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
    var selectedHour by rememberSaveable { mutableStateOf(0) }
    var selectedMinute by rememberSaveable { mutableStateOf(0) }
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var tasks by rememberSaveable { mutableStateOf(listOf<Task>()) }
    if (selectedTime==null && selectedMinute!=0 && selectedHour!=0){
        selectedTime=TimePickerState(selectedHour,selectedMinute,true)
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showMenu) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Spacer(Modifier.padding(top = 24.dp))
                Button(onClick = {
                    showAdvancedExample = true
                    showMenu = false
                }) {
                    Text("Time picker")
                }
                if (selectedTime != null) {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                    cal.set(Calendar.MINUTE, selectedTime!!.minute)
                    selectedMinute=selectedTime!!.minute
                    selectedHour=selectedTime!!.hour
                    cal.isLenient = false
                    Text("Latest Selected time = ${formatter.format(cal.time)}",color = Color.DarkGray)

                    // nakon sto korisnik odabere vreme, zakazemo obavestenje
                    (LocalContext.current as? MainActivity)?.scheduleTaskReminder(cal, "Task Reminder", "Your task is due now!")
                } else {
                    Text("No time selected.",color = Color.DarkGray)
                }

                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(tasks) { task ->
                        ElevatedCardExample(task)
                        Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    }
                }
            }
        }

        when {
            showAdvancedExample -> AdvancedTimePickerExample(
                onDismiss = {
                    showAdvancedExample = false
                    showMenu = true
                },
                onConfirm = { time ->
                    selectedTime = time
                    showAdvancedExample = false
                    showMenu = true
                    if (selectedTime != null) {
                        val cal1 = Calendar.getInstance()
                        cal1.set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                        cal1.set(Calendar.MINUTE, selectedTime!!.minute)
                        cal1.isLenient = false
                        val triggerTimeMillis = cal1.timeInMillis

                        val newTask = Task("task"+(tasks.lastIndex+1), "format: HH:mm:ss", triggerTimeMillis)
                        tasks += newTask
                    }
                },
            )
        }

    }
}

@Composable
fun TaskItem(task: Task) {
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text(text = task.title, style = MaterialTheme.typography.titleSmall,color = colorDarkLight())
        Text(text = task.description, style = MaterialTheme.typography.labelSmall, color = colorDarkLight())
        Text(text = "Reminder set for: ${SimpleDateFormat("HH:mm:ss").format(Date(task.timeMillis))}", color = colorDarkLight())
    }
}

@Composable
fun ElevatedCardExample(task: Task) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth().height(100.dp)
    ) {
        TaskItem(task)
    }
}

@Composable
fun colorDarkLight(): Color {
    if(isSystemInDarkTheme())
        return Color.White
    else
        return Color.DarkGray
}