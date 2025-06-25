package com.example.alarmapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alarmapp.model.Alarm
import com.google.firebase.firestore.FirebaseFirestore


//Holds alarm function defintions



class AlarmRepository {
    private val db = FirebaseFirestore.getInstance()
    private val alarmsRef = db.collection("alarms")

    fun addAlarm(alarm: Alarm) {
        if (alarm.id.isEmpty()) return
        alarmsRef.document(alarm.id).set(alarm)
    }

    fun getAlarms(): LiveData<List<Alarm>> {

        val alarmsLiveData = MutableLiveData<List<Alarm>>()
        alarmsRef.addSnapshotListener { snapshot, _ ->
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
