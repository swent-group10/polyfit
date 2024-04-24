package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController

class Navigation(private val navHostController: NavHostController) {
  fun goBack() {
    navHostController.popBackStack()
  }

  fun navigateToHome() {
    navigateTo(Route.Home)
  }

  fun navigateToNutrition() {
    navigateTo(Route.Nutrition)
  }

  fun navigateToAddMeal() {
    navigateTo(Route.AddMeal)
  }

  fun navigateToAdditionalMealInfo() {
    navigateTo(Route.AdditionalMealInfo)
  }

  private fun navigateTo(route: String) {
    Log.i("Navigation", "Navigating to $route")
    navHostController.navigate(route)
  }
}
