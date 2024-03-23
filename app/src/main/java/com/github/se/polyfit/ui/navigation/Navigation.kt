package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController

class Navigation(private val navHostController: NavHostController) {
  fun goBack() {
    navHostController.popBackStack()
  }

  fun getController(): NavHostController {
    return navHostController
  }

  fun navigateToHome() {
    navigateTo(Route.Home)
  }

  private fun navigateTo(route: String) {
    Log.i("Navigation", "Navigating to $route")
    navHostController.navigate(route)
  }
}
