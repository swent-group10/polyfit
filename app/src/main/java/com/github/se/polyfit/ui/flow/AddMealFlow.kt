package com.github.se.polyfit.ui.flow

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.AdditionalMealInfoScreen
import com.github.se.polyfit.ui.screen.IngredientScreen
import com.github.se.polyfit.ui.screen.NutritionScreen
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@Composable
fun AddMealFlow(
    goBack: () -> Unit,
    navigateToHome: () -> Unit,
    mealViewModel: MealViewModel = hiltViewModel<MealViewModel>()
) {

  val navController = rememberNavController()
  val navigation = Navigation(navController)

  NavHost(navController = navController, startDestination = Route.Ingredients) {
    composable(Route.Ingredients) {
      IngredientScreen(
          mealViewModel = mealViewModel,
          navigateBack = { goBack() },
          navigateForward = navigation::navigateToAdditionalMealInfo)
    }
    composable(Route.AdditionalMealInfo) {
      AdditionalMealInfoScreen(
          mealViewModel = mealViewModel,
          navigateBack = { navigation.goBack() },
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
