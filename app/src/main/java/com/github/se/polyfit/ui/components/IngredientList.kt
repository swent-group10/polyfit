package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.ui.theme.DeleteIconRed
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientList(
    it: PaddingValues,
    mealViewModel: MealViewModel,
) {
  var ingredients = remember { mealViewModel.meal.value?.ingredients ?: emptyList() }

  // TODO: Implement potential ingredients
  val potentialIngredients = listOf<Ingredient>()

  val onAddIngredient = { index: Int -> Log.v("Add Ingredient", "Clicked") }
  val initialSuggestions = 3
  val potentialIndex = remember {
    // equivalent to min(3, potentialIngredients.size)
    mutableIntStateOf(initialSuggestions.coerceAtMost(potentialIngredients.size))
  }

  val lifecycleOwner = LocalLifecycleOwner.current
  mealViewModel.meal.observe(lifecycleOwner) { ingredients = it.ingredients }

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(it)
              .verticalScroll(rememberScrollState())
              .testTag("IngredientsList"),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally) {
        if (ingredients.isEmpty() && potentialIngredients.isEmpty()) {
          Text(
              "No ingredients added.",
              modifier = Modifier.fillMaxSize().padding(16.dp, 0.dp).testTag("NoIngredients"),
              color = MaterialTheme.colorScheme.secondary,
              fontSize = MaterialTheme.typography.headlineSmall.fontSize)
        } else {
          FlowRow(
              horizontalArrangement = Arrangement.Start, verticalArrangement = Arrangement.Top) {
                ingredients.forEach { ingredient ->
                  var isExpanded by remember { mutableStateOf(false) }

                  if (isExpanded) {
                    ExpandedIngredient(
                        ingredient = ingredient,
                        onIngredientRemove = { mealViewModel.removeIngredient(ingredient) },
                        onCollapseIngredient = { isExpanded = false })
                  } else {
                    GradientButton(
                        text = "${ingredient.name} ${ingredient.amount}${ingredient.unit}",
                        onClick = {
                          Log.v("Expand Ingredients", "Clicked")
                          isExpanded = true
                        },
                        active = true,
                        modifier = Modifier.testTag("Ingredient"))
                  }
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

@Composable
fun ExpandedIngredient(
    ingredient: Ingredient,
    onIngredientRemove: () -> Unit,
    onCollapseIngredient: () -> Unit
) {
  GradientBox(
      outerModifier =
          Modifier.testTag("ExpandedIngredient").padding(horizontal = 4.dp, vertical = 1.dp),
      icon = Icons.Filled.Close,
      iconColor = PrimaryPink,
      iconDescriptor = "Delete ${ingredient.name}",
      iconOnClick = onCollapseIngredient) {
        Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 8.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.headlineSmall,
                color = PrimaryPurple,
                modifier = Modifier.padding(start = 2.dp).testTag("ExpandedIngredientName"))

            // TODO: be able to edit the name of the ingredient

            IconButton(
                modifier = Modifier.testTag("DeleteIngredientButton"),
                onClick = {
                  onCollapseIngredient()
                  onIngredientRemove()
                }) {
                  Icon(
                      imageVector = Icons.Default.Delete,
                      contentDescription = "Delete ${ingredient.name}",
                      tint = DeleteIconRed)
                }
          }

          IngredientNutritionEditFields(
              nutritionFields = ingredient.nutritionalInformation.nutrients,
              modifier = Modifier.heightIn(max = 300.dp))
        }
      }
}
