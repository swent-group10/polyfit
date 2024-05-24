package com.github.se.polyfit

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.data.remote.firebase.UserFirebaseRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.flow.SettingFlow
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.CreatePostScreen
import com.github.se.polyfit.ui.screen.DailyRecapScreen
import com.github.se.polyfit.ui.screen.FullGraphScreen
import com.github.se.polyfit.ui.screen.LoginScreen
import com.github.se.polyfit.ui.screen.MapScreen
import com.github.se.polyfit.ui.screen.OverviewScreen
import com.github.se.polyfit.ui.screen.PostInfoScreen
import com.github.se.polyfit.ui.screen.RecipeRecommendationScreen
import com.github.se.polyfit.ui.theme.PolyfitTheme
import com.github.se.polyfit.ui.utils.Authentication
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject lateinit var user: User
  @Inject lateinit var userFirebaseRepository: UserFirebaseRepository
  lateinit var authentication: Authentication

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Log.i("MainActivity", "onCreate")
    // hides the system bar
    WindowCompat.setDecorFitsSystemWindows(window, false)

    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.hide(WindowInsetsCompat.Type.systemBars())

    // Set the behavior to show transient bars by swipe
    controller.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    authentication =
        Authentication(
            activity = this,
            user = user,
            context = this,
            userFirebaseRepository = userFirebaseRepository)

    // TODO: technical debt, next deadline find better way to pass arguments from overview screen
    // to add meal screen
    setContent {
      PolyfitTheme {
        val navController = rememberNavController()
        val navigation = Navigation(navController)
        authentication.setCallbackOnSign { navigation.navigateToHome() }

        val startDestination = if (authentication.isAuthenticated()) Route.Home else Route.Register
        NavHost(navController = navController, startDestination = startDestination) {
          composable(Route.Map) {
            GenericScreen(
                navController = navController,
                content = { paddingValues ->
                  MapScreen(paddingValues, { navController.navigate(Route.PostInfo) })
                })
          }
          composable(Route.Graph) { FullGraphScreen(goBack = navigation::goBack) }
          composable(Route.Home) {
            GenericScreen(
                navController = navController,
                content = { paddingValues -> OverviewScreen(paddingValues, navController) })
          }

          composable(Route.Register) { LoginScreen { authentication.signIn() } }
          composable(Route.AddMeal + "/{mId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mId")
            AddMealFlow(
                goBack = navigation::goBack,
                goForward = { navigation.goBackTo(Route.Home) },
                mealId = mealId)
          }

          composable(Route.EditMeal + "/{mId}") { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mId")
            // If we allow edits from other screens, we will have to modify how we choose goForward
            AddMealFlow(
                goBack = navigation::goBack,
                goForward = { navigation.goBackTo(Route.DailyRecap) },
                mealId = mealId)
          }

          composable(Route.Settings) {
            GenericScreen(
                navController = navController,
                content = {
                  SettingFlow(toLogin = navigation::goBackToLogin, modifier = Modifier.padding(it))
                })
          }

          composable(Route.PostInfo) { PostInfoScreen(navigation, navController) }

          composable(Route.CreatePost) {
            CreatePostScreen(
                navigation::goBack, navigation::navigateToPostList, navigation::navigateToAddMeal)
          }

          composable(Route.DailyRecap) {
            DailyRecapScreen(
                navigateBack = navigation::goBack, navigateTo = navigation::navigateToEditMeal)
          }
          composable(Route.AddMeal) {
            AddMealFlow(navigation::goBack, { navigation.goBackTo(Route.Home) })
          }

          composable(Route.RecipeRecommendation) {
            RecipeRecommendationScreen(navController = navController)
          }
        }
      }
    }
  }
}

@HiltAndroidApp class ExampleApplication : Application()
