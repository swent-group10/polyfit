package com.github.se.polyfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.screen.HomeScreen
import com.github.se.polyfit.ui.screen.LoginScreen

class MainActivity : ComponentActivity() {


    // See: https://developer.android.com/training/basics/intents/result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController: NavHostController = rememberNavController()
            NavHost(navController = navController, startDestination = "register") {
                composable("register") {

                    LoginScreen(navController)
                }

                composable("home") {
                    HomeScreen()
                }
            }
        }
    }
}