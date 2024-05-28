package com.github.se.polyfit.ui.screen.recipeRec

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.R
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.card.IngredientInfoCard
import com.github.se.polyfit.ui.components.card.RecipeInstructions
import com.github.se.polyfit.ui.components.card.TitleAndToggleCard
import com.github.se.polyfit.ui.components.recipe.RecipeCard
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel

@Composable
fun MoreDetailScreen(
    recipeRecViewModel: RecipeRecommendationViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {},
) {
  val context = LocalContext.current

  Scaffold(
      modifier = Modifier.testTag("RecipeDetail"),
      topBar = {
        SimpleTopBar(
            title = ContextCompat.getString(context, R.string.MoreDetailRecipe),
            navigateBack = navigateBack)
      }) { padding ->
        RecipeDetailContent(padding, recipeRecViewModel)
      }
}

@Composable
fun RecipeDetailContent(padding: PaddingValues, recipeRecViewModel: RecipeRecommendationViewModel) {
  val showIngredient by recipeRecViewModel.showIngredient.observeAsState(initial = true)
  val selectedRecipe: Recipe? by recipeRecViewModel.selectedRecipe.observeAsState()

  if (selectedRecipe == null) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
      CircularProgressIndicator(modifier = Modifier.padding(16.dp).testTag("LoadingPost"))
    }
    return
  } else {

    val context = LocalContext.current
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier.testTag("RecipeList")
                .padding(padding)
                .padding(bottom = 16.dp) // Add bottom padding here
        ) {
          item { RecipeCard(recipe = selectedRecipe!!, showTitle = false) }
          item {
            TitleAndToggleCard(
                title = selectedRecipe!!.title,
                button1Title = ContextCompat.getString(context, R.string.Ingredients),
                button2Title = ContextCompat.getString(context, R.string.Recipe),
                recipeRecViewModel)
          }

          if (showIngredient) {
            selectedRecipe?.recipeInformation?.ingredients?.forEach {
              item { IngredientInfoCard(ingredient = it) }
            }
          } else {
            selectedRecipe!!.recipeInformation.instructions.forEachIndexed { index, step ->
              item { RecipeInstructions(step, index + 1) }
            }

            PaddingValues(8.dp)
          }
        }
  }
}
