package com.github.se.polyfit.ui.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.screen.OverviewScreen

fun NavGraphBuilder.globalNavigation(
    navController: NavHostController,
) {
  navigation(startDestination = Route.Home, route = Route.Overview) {
    // TODO: As per Benjo's recommendation, we shouldn't be passing the navController
    composable(Route.Home) {
      GenericScreen(
          navController = navController,
          content = { paddingValues -> OverviewScreen(paddingValues, navController) })
    }
    composable(Route.Map) {
      GenericScreen(navController = navController, content = { Text("Map Screen") })
    }
    composable(Route.Settings) {
      GenericScreen(navController = navController, content = { Text("Settings Screen") })
    }
  }
}
