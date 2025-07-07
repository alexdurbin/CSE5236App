package com.example.alarmapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.Alarm
import com.example.alarmapp.repository.AlarmRepository
//holds alarm function signatures
class AlarmViewModel : ViewModel() {
    private val repo = AlarmRepository()
    val alarms: LiveData<List<Alarm>> = repo.getAlarms()

    fun addAlarm(alarm: Alarm) = repo.addAlarm(alarm)
    fun deleteAlarm(alarm: Alarm) = repo.deleteAlarm(alarm.id)

}
