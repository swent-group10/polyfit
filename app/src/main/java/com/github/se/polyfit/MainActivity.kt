package com.github.se.polyfit

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.AddMealScreen
import com.github.se.polyfit.ui.screen.HomeScreen
import com.github.se.polyfit.ui.screen.LoginScreen
import com.github.se.polyfit.ui.theme.PolyfitTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      PolyfitTheme {
        val navController = rememberNavController()
        val navigation = Navigation(navController)
        NavHost(navController = navController, startDestination = Route.Register) {
          composable(Route.Register) { LoginScreen(navigation::navigateToHome) }
          composable(Route.Home) { HomeScreen() }
          composable(Route.AddMeal) {
            AddMealScreen(mainNavigation = navigation, userID = "testUserID")
          }
        }
      }
    }
  }
}

@HiltAndroidApp class ExampleApplication : Application()
