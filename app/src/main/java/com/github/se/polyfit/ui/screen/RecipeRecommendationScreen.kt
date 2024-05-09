package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.se.polyfit.R
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.components.recipe.RecipeCard
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel

@Composable
fun RecipeRecommendationScreen(
    navController: NavHostController,
    recipeRecViewModel: RecipeRecommendationViewModel = hiltViewModel()
) {
  GenericScreen(
      navController = navController,
      content = { recipeDisplay(recipeRecViewModel) },
      modifier = Modifier.testTag("RecipeDisplay"))
}

@Composable
fun recipeDisplay(recipesRec: RecipeRecommendationViewModel) {
  val context = LocalContext.current

  val recipes = remember { mutableStateOf(listOf<Recipe>()) }
  LaunchedEffect(Unit) { recipes.value = recipesRec.recipeFromIngredients() }

  LazyColumn(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.testTag("RecipeList")) {
        item {
          Text(
              text = ContextCompat.getString(context, R.string.recommendedRecipe),
              style = MaterialTheme.typography.displaySmall,
              fontWeight = FontWeight.Bold,
          )
        }
        recipes.value.forEach { item { RecipeCard(recipe = it) } }
      }
}
