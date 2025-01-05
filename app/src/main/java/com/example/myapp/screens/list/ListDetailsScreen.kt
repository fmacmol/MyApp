package com.example.myapp.screens.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailsScreen(
    navController: NavController,
    listId: String?
) {
    var title by remember { mutableStateOf("") }
    var items by remember { mutableStateOf<MutableList<String>>(mutableListOf()) }
    var newItem by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val db = FirebaseFirestore.getInstance()

    // Cargar datos de la lista desde Firestore
    LaunchedEffect(listId) {
        if (listId != null) {
            db.collection("lists")
                .document(listId)
                .get()
                .addOnSuccessListener { document ->
                    title = document.getString("title") ?: "Sin título"
                    items = (document.get("items") as? List<String>)?.toMutableList() ?: mutableListOf()
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Lista") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (listId != null) {
                    // Guardar los cambios en Firebase
                    db.collection("lists")
                        .document(listId)
                        .update(
                            mapOf(
                                "title" to title,
                                "items" to items
                            )
                        )
                        .addOnSuccessListener {
                            navController.popBackStack() // Regresar al Home después de guardar
                        }
                }
            }) {
                Text("Guardar")
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título de la lista") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(items.size) { index ->
                        EditableListItem(
                            title = items[index],
                            onDelete = { items.removeAt(index) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItem,
                        onValueChange = { newItem = it },
                        label = { Text("Nuevo artículo") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (newItem.isNotEmpty()) {
                            items.add(newItem)
                            newItem = ""
                        }
                    }) {
                        Text("Añadir")
                    }
                }
            }
        }
    }
}

@Composable
fun EditableListItem(title: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar artículo")
            }
        }
    }
}
