package com.example.listatarea

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.listatarea.model.User
import com.example.listatarea.screens.LoginScreen
import com.example.listatarea.screens.RegisterScreen
import com.example.listatarea.screens.HomeScreen

@Composable
fun App() {
    val userState = remember { mutableStateOf<User?>(null) }
    val navigateToRegisterState = remember { mutableStateOf(false) }
    val navigateToHomeState = remember { mutableStateOf(false) }

    var user by userState
    var navigateToRegister by navigateToRegisterState
    var navigateToHome by navigateToHomeState

    if (user == null) {
        if (navigateToRegister) {
            RegisterScreen(
                onRegisterSuccess = { newUser ->
                    user = newUser
                    navigateToHome = true
                    navigateToRegister = false
                },
                onLogin = { navigateToRegister = false },
                onNavigateToHome = { navigateToHome = true }
            )
        } else {
            LoginScreen(
                onLoginSuccess = { newUser ->
                    user = newUser
                    navigateToHome = true
                },
                onRegister = { navigateToRegister = true },
                onNavigateToHome = { navigateToHome = true }
            )
        }
    } else {
        if (navigateToHome) {
            HomeScreen(
               onLogout = {navigateToHome = false }
            )
        } else {
            HomeScreen(
                onLogout = {navigateToHome = false }
            )
        }
    }
}