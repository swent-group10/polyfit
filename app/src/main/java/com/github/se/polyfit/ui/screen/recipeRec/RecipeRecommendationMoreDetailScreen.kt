package com.github.se.polyfit.ui.screen.recipeRec

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.card.IngredientInfoCard
import com.github.se.polyfit.ui.components.card.NutriInfoCard
import com.github.se.polyfit.ui.components.card.TitleAndToggleCard
import com.github.se.polyfit.ui.components.recipe.RecipeCard
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel

@Composable
fun RecipeRecommendationMoreDetailScreen(
    navController: NavHostController,
    recipeRecViewModel: RecipeRecommendationViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {}
) {
  Scaffold(topBar = { SimpleTopBar(title = "More Detail", navigateBack = navigateBack) }) { it ->
    recipeMoreDetailDisplay(recipeRecViewModel.getSelectedRecipe(), it)
  }
}

@Composable
fun recipeMoreDetailDisplay(recipe: Recipe, padding: PaddingValues) {
  val context = LocalContext.current

  LazyColumn(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.testTag("RecipeList").padding(padding)) {
        item { RecipeCard(recipe = recipe, showTitle = false) }
        item {
          TitleAndToggleCard(
              title = recipe.title, button1Title = "Ingredients", button2Title = "Recipe")
        }
        item { NutriInfoCard(recipeInfo = recipe.recipeInformation) }

        recipe.recipeInformation.ingredients.forEach {
          item { IngredientInfoCard(ingredient = it) }
        }
      }
}
