package com.example.alarmapp.mainUI
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//SCREEN FOR ALARM SET
@Composable
fun AlarmSetupScreen(
    hour: String,
    minute: String,
    challengeType: String,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit,
    onChallengeSelect: (String) -> Unit,
    onSetAlarm: () -> Unit
) {
    val hours = (0..23).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set Alarm Time", fontSize = 24.sp)
        Row {
            DropdownSelector("Hour", hour, hours, onHourChange)
            Spacer(modifier = Modifier.width(16.dp))
            DropdownSelector("Minute", minute, minutes, onMinuteChange)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Choose Challenge")
        Row {
            RadioButton(selected = challengeType == "math", onClick = { onChallengeSelect("math") })
            Text("Math")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = challengeType == "qr", onClick = { onChallengeSelect("qr") })
            Text("QR Code")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSetAlarm) {
            Text("Set Alarm")
        }
    }
}
//drop down selector for time selection
@Composable
fun DropdownSelector(
    label: String,
    selectedValue: String,
    items: List<String>,
    onValueSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedValue)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                items.forEach { value ->
                    DropdownMenuItem(onClick = {
                        onValueSelected(value)
                        expanded = false
                    }, text = { Text(value) })
                }
            }
        }
    }
}
