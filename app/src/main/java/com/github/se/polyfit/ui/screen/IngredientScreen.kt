package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.ui.components.GradientButton

data class Ingredient(
    val name: String,
    val quantity: Int,
    val unit: String,
    val probability: Float = 1.0f,
) {}

@Composable
fun IngredientScreen() {
  val ingredients =
      listOf<Ingredient>(
          Ingredient("Olive Oil", 5, "ml"),
          Ingredient("Beef Tenderloin", 50, "g"),
          Ingredient("White Asparagus", 10, "g"),
          Ingredient("Corn", 4, "g"),
          Ingredient("Foie Gras", 100, "g"),
      )

  val potentialIngredients =
      listOf<Ingredient>(
          Ingredient("Carrots", 100, "g"),
          Ingredient("Peas", 100, "g"),
          Ingredient("Worcestershire Sauce", 15, "ml"),
          Ingredient("Salt", 5, "g"),
          Ingredient("Pepper", 5, "g"),
          Ingredient("Garlic", 1, "clove"),
      )

  Scaffold(topBar = { TopBar() }, bottomBar = { BottomBar() }) {
    IngredientList(it, ingredients, potentialIngredients)
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientList(
    it: PaddingValues,
    ingredients: List<Ingredient>,
    potentialIngredients: List<Ingredient>
) {
  val initialSuggestions = 3
  val potentialIndex = remember {
    mutableIntStateOf(initialSuggestions.coerceAtMost(potentialIngredients.size))
  }
  Column(
      modifier = Modifier.fillMaxSize().padding(it).testTag("IngredientScreen"),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally) {
        FlowRow(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.Top,
            maxItemsInEachRow = Int.MAX_VALUE) {
              ingredients.forEach { ingredient ->
                GradientButton(
                    text = "${ingredient.name} ${ingredient.quantity}${ingredient.unit}",
                    onClick = {}, // Expand to see more information
                    active = true)
              }
              potentialIngredients.subList(0, potentialIndex.value).forEach { ingredient ->
                GradientButton(
                    icon = {
                      Icon(
                          imageVector = Icons.Default.Add,
                          contentDescription = "Add ${ingredient.name}",
                          tint = MaterialTheme.colorScheme.secondary)
                    },
                    text = ingredient.name,
                    onClick = { /*TODO*/}, // Add to ingredient list
                    active = false)
              }
              if (potentialIngredients.size > potentialIndex.value) {
                GradientButton(
                    icon = {
                      Icon(
                          imageVector = Icons.Default.MoreVert,
                          contentDescription = "More Options",
                          modifier = Modifier.graphicsLayer(rotationZ = 90f),
                          tint = MaterialTheme.colorScheme.secondary)
                    },
                    onClick = { potentialIndex.value = potentialIngredients.size },
                    active = false)
              }
            }
      }
}

@Composable
fun TopBar() {
  Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
    IconButton(onClick = { /*TODO*/}) { // Go back to previous screen
      Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Back",
          modifier = Modifier.testTag("BackButton"),
          tint = MaterialTheme.colorScheme.primary)
    }
    Text(
        "Ingredients",
        modifier = Modifier.align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.secondary,
        fontSize = MaterialTheme.typography.headlineMedium.fontSize)
  }
}

@Composable
fun BottomBar() {
  Column(
      modifier =
          Modifier.background(MaterialTheme.colorScheme.background)
              .padding(0.dp, 16.dp, 0.dp, 32.dp),
  ) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp),
        contentAlignment = Alignment.CenterEnd) {
          GradientButton(
              onClick = { /*TODO*/}, // Add new ingredient
              active = true,
              round = true,
              icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Ingredient",
                    tint = MaterialTheme.colorScheme.primary)
              })
        }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
      Button(
          onClick = { /*TODO*/}, // Finish adding ingredients
          modifier = Modifier.width(200.dp),
      ) {
        Text(text = "Done", fontSize = 24.sp)
      }
    }
  }
}

@Preview
@Composable
fun IngredientScreenPreview() {
  IngredientScreen()
}
