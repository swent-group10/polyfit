package com.github.se.polyfit.ui.screen

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.ui.components.GradientButton
import com.github.se.polyfit.ui.navigation.Navigation

data class Ingredient(
    val name: String,
    val quantity: Int,
    val unit: String,
    val probability: Float = 1.0f,
) {}

// TODO: Replace with data from a ViewModel
val testInitialIngredients =
    listOf<Ingredient>(
        Ingredient("Olive Oil", 5, "ml"),
        Ingredient("Beef Tenderloin", 50, "g"),
        Ingredient("White Asparagus", 10, "g"),
        Ingredient("Corn", 4, "g"),
        Ingredient("Foie Gras", 100, "g"))

val testInitialPotentialIngredients =
    listOf<Ingredient>(
        Ingredient("Carrots", 100, "g"),
        Ingredient("Peas", 100, "g"),
        Ingredient("Worcestershire Sauce", 15, "ml"),
        Ingredient("Salt", 5, "g"),
        Ingredient("Pepper", 5, "g"),
        Ingredient("Garlic", 1, "clove"),
    )

@Composable
fun IngredientScreen(
    navigation: Navigation,
    initialIngredients: List<Ingredient> = testInitialIngredients,
    initialPotentialIngredients: List<Ingredient> = testInitialPotentialIngredients,
    // TODO:  ingredientsViewModel: IngredientsViewModel = IngredientsViewModel(),
) {

  val ingredients = remember { mutableStateListOf(*initialIngredients.toTypedArray()) }
  val potentialIngredients = remember {
    mutableStateListOf(*initialPotentialIngredients.toTypedArray())
  }

  Scaffold(
      topBar = { TopBar(navigation) },
      bottomBar = { BottomBar() },
      modifier = Modifier.testTag("IngredientScreen")) {
        if (ingredients.isEmpty() && potentialIngredients.isEmpty()) {
          Text(
              "No ingredients added yet",
              modifier = Modifier.fillMaxSize().padding(it).padding(16.dp),
              color = MaterialTheme.colorScheme.secondary,
              fontSize = MaterialTheme.typography.headlineSmall.fontSize)
        } else {
          IngredientList(
              it,
              ingredients,
              potentialIngredients,
              onAddIngredient = { index ->
                val item = potentialIngredients.removeAt(index)
                ingredients.add(item)
              })
        }
      }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientList(
    it: PaddingValues,
    ingredients: List<Ingredient>,
    potentialIngredients: List<Ingredient>,
    onAddIngredient: (Int) -> Unit,
) {
  val initialSuggestions = 3
  val potentialIndex = remember {
    // equivalent to min(3, potentialIngredients.size)
    mutableIntStateOf(initialSuggestions.coerceAtMost(potentialIngredients.size))
  }
  Column(
      modifier = Modifier.fillMaxSize().padding(it).verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally) {
        FlowRow(
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.Top,
            maxItemsInEachRow = Int.MAX_VALUE) {
              ingredients.forEach { ingredient ->
                GradientButton(
                    text = "${ingredient.name} ${ingredient.quantity}${ingredient.unit}",
                    onClick = {}, // TODO: Expand to see more information
                    active = true,
                    modifier = Modifier.testTag("Ingredient"))
              }
              potentialIngredients.take(potentialIndex.intValue).forEachIndexed { index, ingredient
                ->
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
                    modifier = Modifier.testTag("PotentialIngredientButton"))
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

@Composable
fun TopBar(navigation: Navigation) {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(8.dp).testTag("TopBar")) {
        IconButton(
            onClick = { navigation.goBack() },
            content = {
              Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = "Back",
                  modifier = Modifier.testTag("BackButton"),
                  tint = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.testTag("BackButton"))
        Text(
            "Ingredients",
            modifier = Modifier.align(Alignment.CenterVertically).testTag("IngredientTitle"),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize)
      }
}

@Composable
fun BottomBar() {
  Column(
      modifier =
          Modifier.background(MaterialTheme.colorScheme.background)
              .padding(0.dp, 16.dp, 0.dp, 32.dp)
              .testTag("BottomBar"),
  ) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp).testTag("AddIngredientBox"),
        contentAlignment = Alignment.CenterEnd) {
          GradientButton(
              onClick = { Log.v("Add Ingredient", "Clicked") }, // TODO: Add new ingredient
              active = true,
              round = true,
              icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Ingredient",
                    tint = MaterialTheme.colorScheme.primary)
              },
              modifier = Modifier.testTag("AddIngredientButton"))
        }
    Box(
        modifier = Modifier.fillMaxWidth().testTag("DoneBox"),
        contentAlignment = Alignment.Center) {
          Button(
              onClick = {}, // TODO: Finish adding ingredients
              modifier = Modifier.width(200.dp).testTag("DoneButton"),
          ) {
            Text(text = "Done", fontSize = 24.sp)
          }
        }
  }
}

@Preview
@Composable
fun IngredientScreenPreview() {
  IngredientScreen(Navigation(rememberNavController()))
}
