// Janaath Vijithavarnan
// W1979142
package com.example.dermoinspect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dermoinspect.data.repository.AuthRepository
import com.example.dermoinspect.ui.auth.LoginScreen
import com.example.dermoinspect.ui.auth.SignUpScreen
import com.example.dermoinspect.ui.main.MainScreen
import com.example.dermoinspect.ui.theme.DermoInspectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DermoInspectTheme {
                DermoInspectApp()
            }
        }
    }
}

@Composable
fun DermoInspectApp() {
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository() }

    // This is where it determines start destination based on login status
    val startDestination = if (authRepository.isUserLoggedIn()) {
        "main"
    } else {
        "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // This is the Login Screen
        composable("login") {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // This is the Sign Up Screen
        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // This is the Main Screen
        composable("main") {
            MainScreen(
                onLogout = {
                    authRepository.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}