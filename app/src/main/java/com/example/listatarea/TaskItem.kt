package com.example.listatarea

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.listatarea.model.Task

@Composable
fun TaskItem(
    task: Task,
    onDelete: (String) -> Unit,
    onEdit: (Task) -> Unit,
    onStatusChange: (Task, String) -> Unit
) {
    var taskStatus by remember { mutableStateOf(task.status) }

    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp)
                ) {
                    IconButton(
                        onClick = { onEdit(task) }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar tarea")
                    }
                    IconButton(
                        onClick = { onDelete(task.id) }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
                    }

                    // Compañame el Switch para cambiar el estado
                    Switch(
                        checked = taskStatus == "completed",
                        onCheckedChange = { newState ->
                            taskStatus = if (newState) "completed" else "pending"
                            // Actualiza el estado de la tarea en Firestore
                            onStatusChange(
                                task.copy(status = taskStatus),
                                taskStatus
                            )
                        },
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
                            checkedTrackColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
            Text(
                text = "Prioridad: ${task.priority}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${task.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Categoria: ${task.category}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}