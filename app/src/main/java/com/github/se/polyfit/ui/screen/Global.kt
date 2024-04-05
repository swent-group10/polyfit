package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.se.polyfit.ui.components.MainBottomNavBar
import com.github.se.polyfit.ui.components.OverviewTopBar
import com.github.se.polyfit.ui.navigation.BottomNavItem
import com.github.se.polyfit.ui.navigation.Route

@Composable
fun GenericScreens(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
    topBar: @Composable () -> Unit = {}
) {
  Scaffold(
      topBar = topBar,
      bottomBar = {
        MainBottomNavBar(
            items =
                listOf(
                    BottomNavItem(
                        name = "Overview", route = Route.Overview, icon = Icons.Default.Menu),
                    BottomNavItem(name = "Map", route = Route.Map, icon = Icons.Default.Place),
                    BottomNavItem(
                        name = "Settings", route = Route.Settings, icon = Icons.Default.Settings)),
            navController = navController,
            onItemClick = {
                Log.i("Navigation", "Bottom bar navigation to ${it.route}")
                navController.navigate(it.route)
            },
        )
      },
      content = content)
}

fun NavGraphBuilder.globalNavigation(navController: NavHostController) {
  navigation(startDestination = Route.Overview, route = Route.Home) {
    composable(Route.Overview) {
      GenericScreens(
          navController = navController,
          content = { paddingValues -> OverviewContent(paddingValues) },
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
