package com.github.se.polyfit.ui.screen

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.se.polyfit.ui.components.GenericScreens
import com.github.se.polyfit.ui.components.OverviewTopBar
import com.github.se.polyfit.ui.navigation.Route


fun NavGraphBuilder.globalNavigation(navController: NavHostController) {
    navigation(startDestination = Route.Overview, route = Route.Home) {
        composable(Route.Overview) {
            GenericScreens(
                navController = navController,
                content = { paddingValues -> OverviewScreeen(paddingValues) },
                topBar = { OverviewTopBar() })
        }
        composable(Route.Map) {
            GenericScreens(navController = navController, content = { Text("Map Screen") })
        }
        composable(Route.Settings) {
            GenericScreens(navController = navController, content = { Text("Settings Screen") })
        }
    }
}
