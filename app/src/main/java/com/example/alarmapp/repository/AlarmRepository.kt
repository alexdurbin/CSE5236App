package com.example.alarmapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alarmapp.model.Alarm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AlarmRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId: String?
        get() = auth.currentUser?.uid

    fun addAlarm(alarm: Alarm) {
        val uid = userId
        if (uid.isNullOrEmpty() || alarm.id.isEmpty()) return

        db.collection("users")
            .document(uid)
            .collection("alarms")
            .document(alarm.id)
            .set(alarm)
            .addOnSuccessListener {
                Log.d("AlarmRepository", "Alarm added for user $uid: ${alarm.id}")
            }
            .addOnFailureListener { e ->
                Log.e("AlarmRepository", "Failed to add alarm: ${e.message}")
            }
    }

    fun getAlarms(): LiveData<List<Alarm>> {
        val alarmsLiveData = MutableLiveData<List<Alarm>>()
        val uid = userId

        if (uid.isNullOrEmpty()) {
            alarmsLiveData.value = emptyList()
            return alarmsLiveData
        }

        db.collection("users")
            .document(uid)
            .collection("alarms")
            .addSnapshotListener { snapshot, _ ->
                val alarms = snapshot?.toObjects(Alarm::class.java)
                alarmsLiveData.value = alarms ?: emptyList()
            }

        return alarmsLiveData
    }

    fun deleteAlarm(id: String) {
        val uid = userId
        if (uid.isNullOrEmpty() || id.isBlank()) return

        db.collection("users")
            .document(uid)
            .collection("alarms")
            .document(id)
            .delete()
            //debugging for firebase
            .addOnSuccessListener {
                Log.d("AlarmRepository", "Alarm $id deleted for user $uid.")
            }
            .addOnFailureListener { e ->
                Log.e("AlarmRepository", "Failed to delete alarm $id: ${e.message}")
            }
    }
}
