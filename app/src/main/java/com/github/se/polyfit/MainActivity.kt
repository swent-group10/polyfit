package com.github.se.polyfit

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.LoginScreen
import com.github.se.polyfit.ui.screen.OverviewScreen
import com.github.se.polyfit.ui.theme.PolyfitTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hides the system bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())

        // Set the behavior to show transient bars by swipe
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // TODO: technical debt, next deadline find better way to pass arguments from overview screen
        // to add meal screen
        setContent {
            PolyfitTheme {
                val navController = rememberNavController()
                val navigation = Navigation(navController)
                NavHost(navController = navController, startDestination = Route.Home) {
                    composable(Route.Home) {
                        GenericScreen(
                            navController = navController,
                            content = { paddingValues ->
                                OverviewScreen(
                                    paddingValues,
                                    navController
                                )
                            })
                    }

                    composable(Route.Register) { LoginScreen(navigation::navigateToHome) }

                    composable(Route.AddMeal) {
                        // make sure the create is clear

                        // check reall created
                        AddMealFlow(navigation::goBack, navigation::navigateToHome, "testUserID")
                    }
                }
            }
        }
    }
}

@HiltAndroidApp
class ExampleApplication : Application()
