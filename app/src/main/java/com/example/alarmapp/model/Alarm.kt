package com.example.alarmapp.model

//Data stored for each alarm in firebase
data class Alarm(
    val id: String = "",
    val hour: String = "",
    val minute: String = "",
    val challengeType: String = ""
)

