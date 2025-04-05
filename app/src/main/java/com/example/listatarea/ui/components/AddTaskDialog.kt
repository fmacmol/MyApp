package com.example.listatarea.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.listatarea.utils.Category
import com.example.listatarea.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

@Composable
fun AddTaskDialog(
    onTaskAdded: (Task) -> Unit,
    onCancel: () -> Unit
) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val priority = remember { mutableIntStateOf(0) }
    val selectedCategory = remember { mutableStateOf<Category?>(null) }

    val resetFields = {
        title.value = ""
        description.value = ""
        priority.intValue = 0
        selectedCategory.value = null
    }

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text("Nueva Tarea") },
        text = {
            Column {
                // Campos existentes
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

                // Grupo de categorias
                Column(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Text("Categoría:", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Work),
                            onClick = {
                                selectedCategory.value = Category.Work
                            }
                        )
                        Text("Trabajo")
                    }
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Personal),
                            onClick = {
                                selectedCategory.value = Category.Personal
                            }
                        )
                        Text("Personal")
                    }
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Study),
                            onClick = {
                                selectedCategory.value = Category.Study
                            }
                        )
                        Text("Estudio")
                    }
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedCategory.value == Category.Other),
                            onClick = {
                                selectedCategory.value = Category.Other
                            }
                        )
                        Text("Otro")
                    }
                }

                // Campo de prioridad
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
                    val newTask = Task(
                        title = title.value,
                        description = description.value,
                        priority = priority.intValue,
                        category = selectedCategory.value?.name ?: "Other"
                    )
                    onTaskAdded(newTask)
                }
            ) { Text("Aceptar") }
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