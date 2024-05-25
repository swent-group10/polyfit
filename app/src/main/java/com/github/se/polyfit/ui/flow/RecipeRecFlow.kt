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
    recipeRecommendationViewModel: RecipeRecommendationViewModel = hiltViewModel(),
) {
  val navigation = rememberNavController()

  val navAction = Navigation(navigation)

  NavHost(navController = navigation, startDestination = Route.RecipeRecommendation) {
    composable(Route.RecipeRecommendation) {
      RecipeRecommendationScreen(
          navigation,
          recipeRecommendationViewModel,
          navAction::navigateToRecipeRecommendationMore,
      )
    }
    composable(Route.RecipeRecommendationMore) {
      RecipeRecommendationMoreDetailScreen(
          recipeRecommendationViewModel,
          navigateBack = navAction::goBack,
      )
    }
  }
}
