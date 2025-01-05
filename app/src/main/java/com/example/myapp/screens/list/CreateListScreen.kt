package com.example.myapp.screens.list

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapp.screens.login.LoginScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var title by remember { mutableStateOf("") }
    var items by remember { mutableStateOf("") } // Cadena separada por comas (e.g., "Pan, Leche, Huevos")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Lista") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título de la lista") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = items,
                onValueChange = { items = it },
                label = { Text("Artículos (separados por comas)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        viewModel.saveList(
                            userId = userId,
                            title = title,
                            items = items.split(",").map { it.trim() }
                        )
                        navController.popBackStack() // Regresar a la pantalla anterior
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotEmpty() && items.isNotEmpty()
            ) {
                Text("Guardar Lista")
            }
        }
    }
}

fun saveList(userId: String, title: String, items: List<String>) {
    val list = mapOf(
        "userId" to userId,
        "title" to title,
        "items" to items
    )
    FirebaseFirestore.getInstance()
        .collection("lists")
        .add(list)
        .addOnSuccessListener {
            Log.d("MyLogin", "Lista creada exitosamente: ${it.id}")
        }
        .addOnFailureListener { exception ->
            Log.d("MyLogin", "Error al crear lista: ${exception.message}")
        }
}
