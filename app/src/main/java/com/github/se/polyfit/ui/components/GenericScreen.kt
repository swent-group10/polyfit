package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.github.se.polyfit.ui.screen.OverviewScreeen

@Composable
fun GenericScreen(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
    topBar: @Composable () -> Unit = {}
) {
    Scaffold(
        topBar = { OverviewTopBar() },
        bottomBar = { BottomNavigationBar(navController = navController) },
        content = { paddingValues -> OverviewScreeen(paddingValues) }
    )
}