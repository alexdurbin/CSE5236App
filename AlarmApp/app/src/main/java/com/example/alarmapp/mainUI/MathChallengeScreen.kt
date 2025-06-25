package com.example.alarmapp.mainUI


import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import com.example.alarmapp.R


//MATH PROBLEM POP UP SCREEN
@Composable
fun MathChallengeScreen(onSolved: () -> Unit) {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.alarm_sound).apply {
            isLooping = true
            start()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
    //math problem is chosen with multiplication of 2 random single digit nums,
    //and addition of 1 random double digit num
    val a = remember { (1..9).random() }
    val b = remember { (1..9).random() }
    val c = remember { (10..99).random() }
    val answer = remember { a * b + c }

    var userInput by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Solve: $a * $b + $c", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Your Answer") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (userInput.toIntOrNull() == answer) {
                isCorrect = true
                onSolved()
            }
        }) {
            Text("Submit")
        }
        if (isCorrect) {
            Text("Correct! Returning to Main Screen.", fontSize = 16.sp)
        }
    }
}
