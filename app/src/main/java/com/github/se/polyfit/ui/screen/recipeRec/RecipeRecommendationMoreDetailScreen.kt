package com.github.se.polyfit.ui.screen.recipeRec

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.R
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.card.IngredientInfoCard
import com.github.se.polyfit.ui.components.card.NutriInfoCard
import com.github.se.polyfit.ui.components.card.TitleAndToggleCard
import com.github.se.polyfit.ui.components.recipe.RecipeCard
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel

@Composable
fun RecipeRecommendationMoreDetailScreen(
    recipeRecViewModel: RecipeRecommendationViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {},
) {
  val recipe = recipeRecViewModel.getSelectedRecipe()
  val context = LocalContext.current

  Scaffold(
      topBar = {
        SimpleTopBar(
            title = ContextCompat.getString(context, R.string.MoreDetailRecipe),
            navigateBack = navigateBack)
      }) { padding ->
        RecipeDetailContent(recipe, padding, recipeRecViewModel)
      }
}

@Composable
fun RecipeDetailContent(
    recipe: Recipe,
    padding: PaddingValues,
    recipeRecViewModel: RecipeRecommendationViewModel
) {
  val showIngredient by recipeRecViewModel.showIngredient.observeAsState(initial = false)

  val context = LocalContext.current
  LazyColumn(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.testTag("RecipeList").padding(padding)) {
        item { RecipeCard(recipe = recipe, showTitle = false) }
        item {
          TitleAndToggleCard(
              title = recipe.title,
              button1Title = ContextCompat.getString(context, R.string.Ingredients),
              button2Title = ContextCompat.getString(context, R.string.Recipe),
              recipeRecViewModel)
        }
        item { NutriInfoCard(recipeInfo = recipe.recipeInformation) }

        if (showIngredient) {
          recipe.recipeInformation.ingredients.forEach {
            item { IngredientInfoCard(ingredient = it) }
          }
        } else {
          item { recipeInstructions(recipe) }
        }
      }
}

// Place holder will depend on the implementation of the Recipe
@Composable
fun recipeInstructions(recipe: Recipe) {
  Card(onClick = { /*TODO*/}) { Text(recipe.recipeInformation.instructions) }
}
