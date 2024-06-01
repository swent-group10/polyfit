package com.github.se.polyfit.ui.flow

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.recipeRec.MoreDetailScreen
import com.github.se.polyfit.ui.screen.recipeRec.RecommendationScreen
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel

@Composable
fun RecipeRecFlow(
    recipeRecommendationViewModel: RecipeRecommendationViewModel = hiltViewModel(),
    navAction: Navigation
) {
  // Needed because of the nested navigation
  // Will cause a crash if a different one is not used
  val navController = rememberNavController()
  val navigation = Navigation(navController)

  NavHost(navController = navController, startDestination = Route.RecipeRecommendation) {
    composable(Route.RecipeRecommendation) {
      RecommendationScreen(
          navAction.navHostController,
          recipeRecommendationViewModel,
          navigation::navigateToRecipeRecommendationMore,
      )
    }
    composable(Route.RecipeRecommendationMore) {
      MoreDetailScreen(
          recipeRecommendationViewModel,
          navigateBack = navigation::goBack,
      )
    }
  }
}
