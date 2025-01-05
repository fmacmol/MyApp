package com.example.myapp.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapp.navigation.Screens
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var lists by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar listas desde Firebase
    LaunchedEffect(Unit) {
        db.collection("lists")
            .get()
            .addOnSuccessListener { snapshot ->
                lists = snapshot.documents.map { document ->
                    val data = document.data ?: emptyMap()
                    data + ("id" to document.id) // Añadir el ID del documento a los datos
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screens.CreateListScreen.name)
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (lists.isEmpty()) {
                Text("No hay listas disponibles. ¡Crea una nueva!")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(lists.size) { index ->
                        val listId = lists[index]["id"] as? String
                        ListItem(
                            title = lists[index]["title"] as? String ?: "Sin título",
                            onClick = {
                                if (listId != null) {
                                    navController.navigate("${Screens.ListDetailsScreen.name}/$listId")
                                }
                            },
                            onDelete = {
                                if (listId != null) {
                                    db.collection("lists").document(listId)
                                        .delete()
                                        .addOnSuccessListener {
                                            lists = lists.toMutableList().apply { removeAt(index) }
                                        }
                                        .addOnFailureListener { e ->
                                            // Manejar error si es necesario
                                        }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(title: String, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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
            Row {
                Button(onClick = onClick) {
                    Text("Ver")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}
