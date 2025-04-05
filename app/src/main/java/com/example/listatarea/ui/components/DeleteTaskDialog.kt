package com.example.listatarea.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DeleteTaskDialog(
    showDeleteDialog: Boolean,
    selectedTaskId: String?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    if (showDeleteDialog && selectedTaskId != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar esta tarea?") },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(selectedTaskId)
                        onDismiss()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}