package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.github.se.polyfit.R
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.components.recipe.RecipeCard

@Composable
fun RecipeRecommendationScreen(navController: NavHostController, recipes: List<Recipe>) {
  GenericScreen(
      navController = navController,
      content = { recipeDisplay(recipes) },
      modifier = Modifier.testTag("RecipeDisplay"))
}

@Composable
fun recipeDisplay(recipes: List<Recipe>) {
  val context = LocalContext.current

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
        recipes.forEach { item { RecipeCard(recipe = it) } }
      }
}
