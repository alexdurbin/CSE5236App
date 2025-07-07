package com.example.alarmapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.alarmapp.logIn.AuthManager
import com.example.alarmapp.mainUI.*
import com.example.alarmapp.model.Alarm
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

                var loggedIn by remember { mutableStateOf(AuthManager.currentUser != null) }
                var currentScreen by remember { mutableStateOf("main") }
                var alarmHour by remember { mutableStateOf("07") }
                var alarmMinute by remember { mutableStateOf("00") }
                var alarmSet by remember { mutableStateOf(false) }
                var challengeType by remember { mutableStateOf("math") }

                //update the viewmodel when a different person signs in
                LaunchedEffect(loggedIn) {
                    if (loggedIn) {
                        alarmViewModel.setCurrentUser(AuthManager.currentUser?.uid ?: "")
                    } else {
                        alarmViewModel.setCurrentUser("")
                    }
                }

                if (!loggedIn) {
                    LoginScreen(onLoginSuccess = {
                        loggedIn = true
                    })
                } else {

                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(1000)
                            val currentTime = getCurrentTime()
                            if (alarmSet && currentTime.substring(0, 5) == "$alarmHour:$alarmMinute") {
                                currentScreen = when (challengeType) {
                                    "math" -> "math"
                                    "qr" -> "qr"
                                    else -> "main"
                                }
                                alarmSet = false
                            }
                        }
                    }
                    //LIST OF ALARMS SCREEN
                    when (currentScreen) {
                        "main" -> {
                            val alarmList by alarmViewModel.alarms.observeAsState(initial = emptyList())
                            MainScreen(
                                alarms = alarmList,
                                onSetAlarmClick = { currentScreen = "setup" },
                                onDeleteAlarm = { alarm ->
                                    alarmViewModel.deleteAlarm(alarm)
                                    Toast.makeText(context, "Alarm Deleted", Toast.LENGTH_SHORT).show()
                                },
                                onLogout = {
                                    AuthManager.logout()
                                    loggedIn = false
                                }
                            )
                        }
                        //SELECTING TIME SCREENn
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
                                    challengeType = challengeType,
                                    userId = AuthManager.currentUser?.uid ?: ""
                                )
                                alarmViewModel.addAlarm(alarm)
                                Log.d("AlarmDebug", "Adding alarm: $alarm")
                                Toast.makeText(context, "Alarm Set and Saved!", Toast.LENGTH_SHORT).show()
                                currentScreen = "main"
                            }
                        )
                        //MATH PROBLEM SCREEN
                        "math" -> MathChallengeScreen(
                            onSolved = {
                                currentScreen = "main"
                            }
                        )
                        //QR SCAN screen
                        "qr" -> QrChallengeScreen(
                            expectedCode = "alarm-qr-auth-1234",
                            onSolved = {
                                currentScreen = "main"
                            }
                        )
                    }
                }
            }
        }
    }
}

fun getCurrentTime(): String {
    val sdf = java.text.SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
