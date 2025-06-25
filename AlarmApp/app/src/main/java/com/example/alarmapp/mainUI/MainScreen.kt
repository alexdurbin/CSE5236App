package com.example.alarmapp.mainUI

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alarmapp.model.Alarm
import kotlinx.coroutines.delay


//LIST OF ALARMS MAIN PAGE
@Composable
fun MainScreen(
    alarms: List<Alarm>,
    onSetAlarmClick: () -> Unit,
    onDeleteAlarm: (Alarm) -> Unit
) {
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = getCurrentTime()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Current Time: $currentTime", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Alarms Set:", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))

        alarms.forEach { alarm ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("${alarm.hour}:${alarm.minute} - ${alarm.challengeType}", fontSize = 16.sp)
                Button(onClick = { onDeleteAlarm(alarm) }) {
                    Text("Delete")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSetAlarmClick) {
            Text("Set Alarm")
        }
    }
}

// Helper function if used only here
fun getCurrentTime(): String {
    val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}
