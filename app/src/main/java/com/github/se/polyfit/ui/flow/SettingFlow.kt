package com.github.se.polyfit.ui.flow

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.model.settings.SettingOption
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.SettingsScreen
import com.github.se.polyfit.ui.screen.settings.AccountSettingsScreen

@Composable
fun SettingFlow() {
  val navController = rememberNavController()
  val navigation = Navigation(navController)
  val options =
      listOf(
          SettingOption("Account", Icons.Default.Person, navigation::navigateToAccountSettings),
      )

  NavHost(navController = navController, startDestination = Route.SettingsHome) {
    composable(Route.SettingsHome) { SettingsScreen(settings = options) }

    composable(Route.AccountSettings) { AccountSettingsScreen(navigation::navigateToSettingsHome) }
  }
}
