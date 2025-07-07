package com.example.alarmapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alarmapp.model.Alarm
import com.google.firebase.firestore.FirebaseFirestore
//holds get, set, add, delete function definitions for alarms
class AlarmRepository {

    private val db = FirebaseFirestore.getInstance()
    private val alarmsRef = db.collection("alarms")


    fun addAlarm(alarm: Alarm) {
        if (alarm.id.isEmpty() || alarm.userId.isEmpty()) return
        alarmsRef.document(alarm.id).set(alarm)
            .addOnSuccessListener {
                Log.d("AlarmRepository", "Alarm ${alarm.id} saved successfully.")
            }

            .addOnFailureListener { e ->
                Log.e("AlarmRepository", "Failed to save alarm ${alarm.id}: ${e.message}")
            }
    }

    fun getAlarmsForUser(userId: String): LiveData<List<Alarm>> {
        val alarmsLiveData = MutableLiveData<List<Alarm>>()
        alarmsRef.whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {

                    Log.e("AlarmRepository", "Error fetching alarms: ${error.message}")
                    alarmsLiveData.value = emptyList()
                    return@addSnapshotListener

                }
                val alarms = snapshot?.toObjects(Alarm::class.java)
                alarmsLiveData.value = alarms ?: emptyList()
            }
        return alarmsLiveData
    }

    fun deleteAlarm(id: String) {
        if (id.isBlank()) return
        alarmsRef.document(id).delete()

            .addOnSuccessListener {
                Log.d("AlarmRepository", "Alarm $id deleted successfully.")
            }
            .addOnFailureListener { e ->
                Log.e("AlarmRepository", "Failed to delete alarm $id: ${e.message}")
            }
    }
}
