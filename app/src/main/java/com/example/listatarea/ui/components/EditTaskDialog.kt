package com.example.listatarea.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.listatarea.utils.Category
import com.example.listatarea.model.Task

@Composable
fun EditTaskDialog(
    task: Task,
    onTaskUpdated: (Task) -> Unit,
    onCancel: () -> Unit
) {
    val title = remember { mutableStateOf(task.title) }
    val description = remember { mutableStateOf(task.description) }
    val priority = remember { mutableIntStateOf(task.priority) }
    val selectedCategory = remember { mutableStateOf(Category.valueOf(task.category)) }

    val resetFields = {
        title.value = task.title
        description.value = task.description
        priority.intValue = task.priority
        selectedCategory.value = Category.valueOf(task.category)
    }

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text("Editar Tarea") },
        text = {
            Column {
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Título") }
                )
                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Descripción") }
                )

                Column(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Text("Categoría:", style = MaterialTheme.typography.titleSmall)
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Work),
                            onClick = {
                                selectedCategory.value = Category.Work
                            }
                        )
                        Text("Trabajo")
                    }
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Personal),
                            onClick = {
                                selectedCategory.value = Category.Personal
                            }
                        )
                        Text("Personal")
                    }
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Study),
                            onClick = {
                                selectedCategory.value = Category.Study
                            }
                        )
                        Text("Estudio")
                    }
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Other),
                            onClick = {
                                selectedCategory.value = Category.Other
                            }
                        )
                        Text("Otro")
                    }
                }

                Column(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Text("Prioridad:", style = MaterialTheme.typography.titleSmall)
                    Slider(
                        value = priority.intValue.toFloat(),
                        onValueChange = {
                            priority.intValue = it.roundToInt()
                        },
                        valueRange = 0f..5f,
                        steps = 5
                    )
                    Text("Prioridad: ${priority.intValue}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedTask = Task(
                        id = task.id,
                        title = title.value,
                        description = description.value,
                        priority = priority.intValue,
                        category = selectedCategory.value.name,
                        status = task.status
                    )
                    onTaskUpdated(updatedTask)
                }
            ) { Text("Guardar cambios") }
        },
        dismissButton = {
            Button(
                onClick = {
                    resetFields()
                    onCancel()
                }
            ) { Text("Cancelar") }
        }
    )
}