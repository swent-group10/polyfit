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
import androidx.room.Room
import com.github.se.polyfit.data.local.database.MealDatabase
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.navigation.globalNavigation
import com.github.se.polyfit.ui.screen.LoginScreen
import com.github.se.polyfit.ui.theme.PolyfitTheme
import com.github.se.polyfit.viewmodel.meal.MealViewModel
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

    // TO DO: technical debt, next deadline find better way to pass arguments from overview screen
    // to add meal screen

    var mealViewModel =
        MealViewModel(
            MealRepository(
                this.applicationContext,
                MealFirebaseRepository("testUserID"),
                Room.databaseBuilder(
                        this.applicationContext, MealDatabase::class.java, "meal_database")
                    .build()
                    .mealDao()))
    setContent {
      PolyfitTheme {
        val navController = rememberNavController()
        val navigation = Navigation(navController)
        NavHost(navController = navController, startDestination = Route.Register) {
          globalNavigation(navController, mealViewModel)
          composable(Route.Register) { LoginScreen(navigation::navigateToHome) }

          composable(Route.AddMeal) {
            // make sure the create is clear

            // check reall created
            AddMealFlow(navigation::goBack, navigation::navigateToHome, "testUserID", mealViewModel)
          }
        }
      }
    }
  }
}

@HiltAndroidApp class ExampleApplication : Application()
