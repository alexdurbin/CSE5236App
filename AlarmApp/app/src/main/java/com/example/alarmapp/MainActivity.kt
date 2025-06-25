package com.example.alarmapp
import androidx.compose.runtime.livedata.observeAsState

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.alarmapp.mainUI.MathChallengeScreen
import com.example.alarmapp.model.Alarm
import com.example.alarmapp.mainUI.AlarmSetupScreen
import com.example.alarmapp.mainUI.MainScreen
import com.example.alarmapp.ui.theme.AlarmAppTheme
import com.example.alarmapp.viewmodel.AlarmViewModel
import kotlinx.coroutines.delay
import java.util.*

class MainActivity : ComponentActivity() {

    private val alarmViewModel: AlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlarmAppTheme {
                val context = LocalContext.current
                var currentScreen by remember { mutableStateOf("main") }
                var alarmHour by remember { mutableStateOf("07") }
                var alarmMinute by remember { mutableStateOf("00") }
                var alarmSet by remember { mutableStateOf(false) }
                var challengeType by remember { mutableStateOf("math") }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(1000)
                        val currentTime = getCurrentTime()
                        if (alarmSet && currentTime.substring(0, 5) == "$alarmHour:$alarmMinute") {
                            currentScreen = when (challengeType) {
                                "math" -> "math"
                                else -> "main" //placeholder for QR
                            }
                            alarmSet = false
                        }
                    }
                }

                when (currentScreen) {
                    "main" -> {
                        val alarmList by alarmViewModel.alarms.observeAsState(initial = emptyList())

                        MainScreen(
                            alarms = alarmList,
                            onSetAlarmClick = { currentScreen = "setup" },
                            onDeleteAlarm = { alarm ->
                                alarmViewModel.deleteAlarm(alarm)
                                Toast.makeText(context, "Alarm Deleted", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    "setup" -> AlarmSetupScreen(
                        hour = alarmHour,
                        minute = alarmMinute,
                        challengeType = challengeType,
                        onHourChange = { alarmHour = it },
                        onMinuteChange = { alarmMinute = it },
                        onChallengeSelect = { challengeType = it },
                        onSetAlarm = {
                            alarmSet = true
                            val alarm = Alarm(
                                id = UUID.randomUUID().toString(),
                                hour = alarmHour,
                                minute = alarmMinute,
                                challengeType = challengeType
                            )
                            alarmViewModel.addAlarm(alarm)
                            Toast.makeText(context, "Alarm Set and Saved!", Toast.LENGTH_SHORT).show()
                            currentScreen = "main"
                        }
                    )
                    //back to main screen when problem solved/shut off alarm
                    "math" -> MathChallengeScreen(onSolved = {
                        currentScreen = "main"
                    })
                }
            }
        }
    }
}

fun getCurrentTime(): String {
    val sdf = java.text.SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
