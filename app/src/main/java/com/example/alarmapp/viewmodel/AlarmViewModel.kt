package com.example.alarmapp.viewmodel

import androidx.lifecycle.*
import com.example.alarmapp.model.Alarm
import com.example.alarmapp.repository.AlarmRepository

class AlarmViewModel : ViewModel() {
    private val repo = AlarmRepository()

    //holds the current user UID
    private val _currentUserId = MutableLiveData<String>()

    //liveData of alarms filtered by the current user, automatically updates when userId changes
    val alarms: LiveData<List<Alarm>> = _currentUserId.switchMap { userId ->
        if (userId.isBlank()) {
            MutableLiveData(emptyList())
        } else {
            repo.getAlarmsForUser(userId)
        }
    }

    //updates userId whenever the person signing in changes
    fun setCurrentUser(userId: String) {
        _currentUserId.value = userId
    }

    fun addAlarm(alarm: Alarm) = repo.addAlarm(alarm)

    fun deleteAlarm(alarm: Alarm) = repo.deleteAlarm(alarm.id)
}
