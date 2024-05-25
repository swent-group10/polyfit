package com.github.se.polyfit.ui.screen.recipeRec

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
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
    recipeRecViewModel: RecipeRecommendationViewModel = hiltViewModel(),
    navigateToRecipeRecommendationMore: () -> Unit = {},
) {
  GenericScreen(
      navController = navController,
      content = { it -> recipeDisplay(recipeRecViewModel, it, navigateToRecipeRecommendationMore) },
      modifier = Modifier.testTag("RecipeDisplay"))
}

@Composable
fun recipeDisplay(
    recipesRec: RecipeRecommendationViewModel,
    paddingValues: PaddingValues,
    navigateToRecipeRecommendationMore: () -> Unit
) {
  val context = LocalContext.current

  val recipes = remember { mutableStateOf(listOf<Recipe>()) }
  LaunchedEffect(Unit) {
    recipes.value = recipesRec.recipeFromIngredients(recipesRec.ingredientList())
  }

  LazyColumn(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.testTag("RecipeList").padding(paddingValues)) {
        item {
          Text(
              text = ContextCompat.getString(context, R.string.recommendedRecipe),
              style = MaterialTheme.typography.displaySmall,
              fontWeight = FontWeight.Bold,
          )
        }
        if (recipes.value.isEmpty()) {
          item { loader(paddingValues) }
          return@LazyColumn
        }

        recipes.value.forEach {
          item {
            RecipeCard(
                recipe = it,
                onCardClick = {
                  (recipesRec::onSelectedRecipe)(it)
                  navigateToRecipeRecommendationMore.invoke()
                })
          }
        }
      }
}

@Composable
fun loader(paddingValues: PaddingValues) {

  Box(
      modifier = Modifier.fillMaxSize().padding(paddingValues),
      contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp).testTag("LoadingPost"))
      }
  return
}
