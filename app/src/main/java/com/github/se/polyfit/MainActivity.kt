package com.github.se.polyfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.HomeScreen
import com.github.se.polyfit.ui.screen.LoginScreen
import com.github.se.polyfit.ui.theme.PolyfitTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      PolyfitTheme {
        val navController = rememberNavController()
        val navigation = Navigation(navController)
        NavHost(navController = navController, startDestination = Route.Register) {
          composable(Route.Register) { LoginScreen(navigation) }

          composable(Route.Home) { HomeScreen() }
        }
      }
    }
  }
}
