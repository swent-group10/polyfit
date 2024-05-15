package com.github.se.polyfit.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.se.polyfit.ui.components.scaffold.AppTitle
import com.github.se.polyfit.ui.components.scaffold.BottomNavigationBar
import com.github.se.polyfit.ui.navigation.Route

@Composable
fun GenericScreen(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    floatingButton: @Composable () -> Unit = {}
) {
  val context: Context = LocalContext.current
  val stackEntry by navController.currentBackStackEntryAsState()
  Scaffold(
      modifier = modifier,
      topBar = { AppTitle() },
      floatingActionButton = floatingButton,
      bottomBar = {
        BottomNavigationBar(
            stackEntry,
            { navController.navigate(Route.Home) },
            { navController.navigate(Route.PostInfo) },
            { navController.navigate(Route.Settings) },
            {})
      }) {
        Box(modifier = Modifier.padding(it)) { content(it) }
      }
}
