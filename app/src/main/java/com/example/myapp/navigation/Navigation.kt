package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.screens.splash.SplashScreen
import com.example.myapp.screens.login.LoginScreen
import com.example.myapp.screens.home.Home
import com.example.myapp.screens.list.CreateListScreen
import com.example.myapp.screens.list.ListDetailsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.name
    ) {
        composable(Screens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(Screens.LoginScreen.name){
            LoginScreen(navController = navController)
        }
        composable(Screens.HomeScreen.name){
            Home(navController = navController)
        }
        composable(Screens.CreateListScreen.name) {
            CreateListScreen(navController = navController)
        }
        composable(Screens.ListDetailsScreen.name + "/{listId}") { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId")
            ListDetailsScreen(navController = navController, listId = listId)
        }

    }
}