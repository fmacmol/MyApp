package com.example.listatarea.screens

import android.util.Log
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.listatarea.TaskItem
import com.example.listatarea.model.Task
import com.example.listatarea.ui.components.AddTaskDialog
import com.example.listatarea.ui.components.DeleteTaskDialog
import com.example.listatarea.ui.components.EditTaskDialog
import com.example.listatarea.util.FirebaseHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val tasksState = remember { mutableStateListOf<Task>() }
    val firebaseHelper = remember { FirebaseHelper() }
    val scope = rememberCoroutineScope()
    var incompleteTasks by remember { mutableStateOf(0) }
    var allTasks by remember { mutableStateOf(0) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showEditTaskDialog by remember { mutableStateOf(false) }
    var currentTaskToEdit by remember { mutableStateOf(Task()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedTaskId by remember { mutableStateOf<String?>(null) }
    val currentUserId = remember { Firebase.auth.currentUser?.uid }

    // Función para recalcular los contadores
    fun updateCounters() {
        incompleteTasks = tasksState.count { it.status != "completed" }
        allTasks = tasksState.size
    }

    /*LaunchedEffect(Unit) {
        val tasks = firebaseHelper.getTasks()
        tasksState.clear()
        tasksState.addAll(tasks)
        updateCounters()
    }*/

    LaunchedEffect(Unit) {
        currentUserId?.let { uid ->
            val tasks = firebaseHelper.getTasks(uid)
            tasksState.clear()
            tasksState.addAll(tasks)
            updateCounters()
        }
    }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(text = "Tareas") },
            actions = {
                Text(text = "Tareas pendientes: $incompleteTasks de $allTasks")
            }
        )},

        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                content = { Icon(Icons.Default.Add, contentDescription = "Añadir tarea") }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(tasksState) { task ->
                TaskItem(
                    task = task,
                    onDelete = { taskId ->
                        selectedTaskId = taskId
                        showDeleteDialog = true
                    },
                    onEdit = { task ->
                        showEditTaskDialog = true
                        currentTaskToEdit = task
                    },
                    onStatusChange = { updatedTask, newStatus ->
                        scope.launch {
                            try {
                                firebaseHelper.updateTaskStatus(updatedTask.id, newStatus)
                                val index = tasksState.indexOfFirst { it.id == updatedTask.id }
                                if (index != -1) {
                                    tasksState[index] = updatedTask
                                    updateCounters() // Actualizar contadores
                                }
                            } catch (e: Exception) {
                                Log.e("HomeScreen", "Error updating task status", e)
                            }
                        }
                    }
                )
            }
        }
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onTaskAdded = {
                showAddTaskDialog = false
                val newTask = it.copy(id = "")

                scope.launch {
                    try {
                        val taskId = firebaseHelper.addTask(newTask)
                        val taskWithId = newTask.copy(id = taskId)
                        if (!tasksState.contains(taskWithId)) {
                            tasksState.add(taskWithId)
                            updateCounters() // Actualizar contadores
                        }
                    } catch (e: Exception) {
                        Log.e("HomeScreen", "Error adding task", e)
                    }
                }
            },
            onCancel = {
                showAddTaskDialog = false
            }
        )
    }

    if (showEditTaskDialog) {
        EditTaskDialog(
            task = currentTaskToEdit,
            onTaskUpdated = { updatedTask ->
                showEditTaskDialog = false
                scope.launch {
                    try {
                        firebaseHelper.updateTask(updatedTask)
                        val index = tasksState.indexOfFirst { it.id == updatedTask.id }
                        if (index != -1) {
                            tasksState[index] = updatedTask
                            updateCounters() // Actualizar contadores
                        }
                    } catch (e: Exception) {
                        Log.e("HomeScreen", "Error updating task", e)
                    }
                }
            },
            onCancel = {
                showEditTaskDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        DeleteTaskDialog(
            showDeleteDialog = showDeleteDialog,
            selectedTaskId = selectedTaskId,
            onDismiss = {
                showDeleteDialog = false
                selectedTaskId = null
            },
            onConfirm = { taskId ->
                scope.launch {
                    try {
                        firebaseHelper.deleteTask(taskId)
                        tasksState.removeIf { it.id == taskId }
                        updateCounters() // Actualizar contadores
                    } catch (e: Exception) {
                        Log.e("HomeScreen", "Error deleting task", e)
                    }
                }
            }
        )
    }
}