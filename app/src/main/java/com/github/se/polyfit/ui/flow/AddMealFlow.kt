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

/**
 * AddMealFlow is the flow for adding a meal. The flow consists of three screens: IngredientScreen,
 * AdditionalMealInfoScreen, and NutritionScreen. The user can navigate between the screens by
 * clicking the next button.
 *
 * @param goBack The function to navigate back to where the flow was started
 * @param goForward The function to go to the desired screen after the flow is completed
 * @param mealId The id of the meal to edit
 * @param mealViewModel The view model for the meal
 */
@Composable
fun AddMealFlow(
    goBack: () -> Unit,
    goForward: () -> Unit,
    mealId: String? = null,
    mealViewModel: MealViewModel = hiltViewModel<MealViewModel>(),
) {
  val navController = rememberNavController()
  val navigation = Navigation(navController)
  mealViewModel.setMealData(mealId)

  NavHost(navController = navController, startDestination = Route.Ingredients) {
    composable(Route.Ingredients) {
      IngredientScreen(
          mealViewModel = mealViewModel,
          navigateBack = goBack,
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
          navigateForward = { goForward() })
    }
  }
}
