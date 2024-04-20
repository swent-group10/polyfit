package com.github.se.polyfit.ui.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.screen.Map
import com.github.se.polyfit.ui.screen.OverviewScreen
import com.github.se.polyfit.viewmodel.meal.MealViewModel

fun NavGraphBuilder.globalNavigation(
    navController: NavHostController,
    mealViewModel: MealViewModel
) {
  navigation(startDestination = Route.Home, route = Route.Overview) {
    composable(Route.Home) {
      GenericScreen(
          navController = navController,
          content = { paddingValues ->
            OverviewScreen(paddingValues, navController, mealViewModel)
          })
    }
    composable(Route.Map) { Map() }
    composable(Route.Settings) {
      GenericScreen(navController = navController, content = { Text("Settings Screen") })
    }
  }
}
