package com.github.se.polyfit.ui.flow

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.recipeRec.RecipeRecommendationMoreDetailScreen
import com.github.se.polyfit.ui.screen.recipeRec.RecipeRecommendationScreen
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel

@Composable
fun RecipeRecFlow(
    goBack: () -> Unit,
    goForward: () -> Unit,
    recipeRecommendationViewModel: RecipeRecommendationViewModel = hiltViewModel(),
) {
  val navController = rememberNavController()
  val navigation = Navigation(navController)

  NavHost(navController = navController, startDestination = Route.RecipeRecommendation) {
    composable(Route.RecipeRecommendation) {
      RecipeRecommendationScreen(
          navController,
          recipeRecommendationViewModel,
          navigation::navigateToRecipeRecommendationMore,
      )
    }
    composable(Route.RecipeRecommendationMore) {
      RecipeRecommendationMoreDetailScreen(
          navController,
          recipeRecommendationViewModel,
      )
    }
  }
}
