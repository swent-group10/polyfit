package com.github.se.polyfit.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController

class Navigation(private val navHostController: NavHostController) {
  fun goBack() {
    navHostController.popBackStack()
  }

  fun navigateToPostList() {
    navigateTo(Route.PostInfo)
  }

  fun goBackTo(route: String) {
    navHostController.popBackStack(route, inclusive = false)
  }

  fun restartToLogin() {
    navHostController.navigate(Route.Register) {
      popUpTo(navHostController.graph.startDestinationId) { inclusive = true }
      launchSingleTop = true
    }
  }

  fun navigateToGraph() {
    navigateTo(Route.Graph)
  }

  fun navigateToRecipeRecommendationMore() {
    navigateTo(Route.RecipeRecommendationMore)
  }

  fun navigateToEditMeal(mealDatabaseId: String) {
    navigateTo(Route.EditMeal + "/$mealDatabaseId")
  }

  fun navigateToHome() {
    navigateTo(Route.Home)
  }

  fun navigateToNutrition() {
    navigateTo(Route.Nutrition)
  }

  fun navigateToAddMeal(mealDatabaseId: String? = null) {
    if (mealDatabaseId.isNullOrEmpty()) {
      navigateTo(Route.AddMeal)
    } else {
      navigateTo(Route.AddMeal + "/$mealDatabaseId")
    }
  }

  fun navigateToAddMeal() {
    navigateToAddMeal(null)
  }

  fun navigateToAdditionalMealInfo() {
    navigateTo(Route.AdditionalMealInfo)
  }

  fun navigateToDailyRecap() {
    navigateTo(Route.DailyRecap)
  }

  fun navigateToCreatePost() {
    navigateTo(Route.CreatePost)
  }

  fun navigateToSettingsHome() {
    navigateTo(Route.SettingsHome)
  }

  fun navigateToAccountSettings() {
    navigateTo(Route.AccountSettings)
  }

  private fun navigateTo(route: String) {
    Log.i("Navigation", "Navigating to $route")
    navHostController.navigate(route)
  }
}
