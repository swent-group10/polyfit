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

  fun navigateToAddMeal(mealDatabaseId: Long? = null) {
    if (mealDatabaseId == null) {
      navigateTo(Route.AddMeal)
    } else {
      navigateTo(Route.AddMeal + "/$mealDatabaseId")
    }
  }

  fun navigateToAdditionalMealInfo() {
    navigateTo(Route.AdditionalMealInfo)
  }

  private fun navigateTo(route: String) {
    Log.i("Navigation", "Navigating to $route")
    navHostController.navigate(route)
  }
}
