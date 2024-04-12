package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientList(
    it: PaddingValues,
    mealViewModel: MealViewModel,
) {

  val ingredientsRemember = remember { mutableStateListOf<Ingredient>() }

  // TODO: Implement potential ingredients
  val potentialIngredients = listOf<Ingredient>()
  // link potential ingredients to the mealViewModel live meal data

  mealViewModel.meal.observeForever {
    // update potential ingredients
    it.ingredients.forEach { ingredient: Ingredient -> ingredientsRemember.add(ingredient) }
  }

  val onAddIngredient = { index: Int -> Log.v("Add Ingredient", "Clicked") }
  val initialSuggestions = 3
  val potentialIndex = remember {
    // equivalent to min(3, potentialIngredients.size)
    mutableIntStateOf(initialSuggestions.coerceAtMost(potentialIngredients.size))
  }

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(it)
              .verticalScroll(rememberScrollState())
              .testTag("IngredientsList"),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally) {
        if (ingredientsRemember.isEmpty() && potentialIngredients.isEmpty()) {
          Text(
              "No ingredients added.",
              modifier = Modifier.fillMaxSize().padding(16.dp, 0.dp).testTag("NoIngredients"),
              color = MaterialTheme.colorScheme.secondary,
              fontSize = MaterialTheme.typography.headlineSmall.fontSize)
        } else {
          FlowRow(
              horizontalArrangement = Arrangement.Start, verticalArrangement = Arrangement.Top) {
                ingredientsRemember.forEach { ingredient ->
                  GradientButton(
                      text = "${ingredient.name} ${ingredient.amount}${ingredient.unit}",
                      onClick = {
                        Log.v("Expand Ingredients", "Clicked")
                      }, // TODO: Expand to see more information
                      active = true,
                      modifier = Modifier.testTag("Ingredient"))
                }
                potentialIngredients.take(potentialIndex.intValue).forEachIndexed {
                    index,
                    ingredient ->
                  GradientButton(
                      icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add ${ingredient.name}",
                            tint = MaterialTheme.colorScheme.secondary)
                      },
                      text = ingredient.name,
                      onClick = { onAddIngredient(index) },
                      active = false,
                      modifier = Modifier.testTag("PotentialIngredient"))
                }
                if (potentialIngredients.size > potentialIndex.intValue) {
                  GradientButton(
                      icon = {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            modifier = Modifier.graphicsLayer(rotationZ = 90f),
                            tint = MaterialTheme.colorScheme.secondary)
                      },
                      onClick = { potentialIndex.intValue = potentialIngredients.size },
                      active = false,
                      modifier = Modifier.testTag("MoreIngredientsButton"))
                }
              }
        }
      }
}
