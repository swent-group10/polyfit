package com.github.se.polyfit.ui.flow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.IngredientScreen
import com.github.se.polyfit.ui.screen.NutritionScreen
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@Composable
fun AddMealFlow(
    goBack: () -> Unit,
    navigateToHome: () -> Unit,
    userID: String,
    mealViewModel: MealViewModel = MealViewModel(userID)
) {
  val navController = rememberNavController()
  val navigation = Navigation(navController)

  NavHost(navController = navController, startDestination = Route.Ingredients) {
    composable(Route.Ingredients) {
      IngredientScreen(
          mealViewModel = mealViewModel,
          navigateBack = { goBack() },
          navigateForward = navigation::navigateToNutrition)
    }
    composable(Route.Nutrition) {
      NutritionScreen(
          mealViewModel = mealViewModel,
          navigateBack = { navigation.goBack() },
          navigateForward = { navigateToHome() })
    }
  }
}