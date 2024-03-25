package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController

class Navigation(private val navHostController: NavHostController) {
  private fun navigateTo(route: String) {
    Log.i("Navigation", "Navigating to $route")
    navHostController.navigate(route)
  }

  fun goBack() {
    navHostController.popBackStack()
  }

  fun navigateToHome() {
    navigateTo(Route.Home)
  }

  fun navigateToIngredients() {
    navigateTo(Route.Ingredients)
  }
}
