package com.example.listatarea.util

import com.google.firebase.firestore.FirebaseFirestore
import com.example.listatarea.model.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class FirebaseHelper {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getTasks(uid: String): List<Task> {
        val querySnapshot = db.collection("tasks")
            .whereEqualTo("uid", uid)
            .get()
            .await()
        return querySnapshot.documents.map { document ->
            Task(
                id = document.id,
                title = document.data?.get("title") as? String ?: "",
                description = document.data?.get("description") as? String ?: "",
                status = document.data?.get("status") as? String ?: "pending",
                priority = (document.data?.get("priority") as? Long ?: 0L).toInt(),
                category = document.data?.get("category") as? String ?: "Other",
                uid = document.data?.get("uid") as? String ?: ""
            )
        }
    }

    suspend fun addTask(task: Task): String {
        val documentReference = db.collection("tasks")
            .add(task.copy(uid = Firebase.auth.currentUser?.uid ?: ""))
            .await()
        return documentReference.id
    }

    // Modificar los demás métodos para incluir el uid
    suspend fun deleteTask(taskId: String) {
        db.collection("tasks").document(taskId).delete().await()
    }

    suspend fun updateTask(task: Task) {
        db.collection("tasks").document(task.id)
            .set(task)
            .await()
    }

    suspend fun updateTaskStatus(taskId: String, newStatus: String) {
        db.collection("tasks")
            .document(taskId)
            .update(mapOf("status" to newStatus))
            .await()
    }
}