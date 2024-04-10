package com.github.se.polyfit.ui.components

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.github.se.polyfit.ui.screen.OverviewScreeen

@Composable
fun GenericScreen(
    navController: NavHostController,
) {

  Scaffold(
      topBar = { AppTitle() },
      bottomBar = { BottomNavigationBar(navController = navController) },
      content = { paddingValues -> OverviewScreeen(paddingValues) })
}
