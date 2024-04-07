package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.ui.components.GradientButton
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.theme.PrimaryPurple

// TODO: Replace with real data class
data class Ingredient(
    val name: String,
    val quantity: Int,
    val unit: String,
    val probability: Float = 1.0f,
) {}

@Composable
fun IngredientScreen(
    navigation: Navigation,
    initialIngredients: List<Ingredient>,
    initialPotentialIngredients: List<Ingredient>,
    // TODO:  ingredientsViewModel: IngredientsViewModel = IngredientsViewModel(),
) {

  val ingredients = remember { mutableStateListOf(*initialIngredients.toTypedArray()) }
  val potentialIngredients = remember {
    mutableStateListOf(*initialPotentialIngredients.toTypedArray())
  }

  Scaffold(topBar = { TopBar(navigation) }, bottomBar = { BottomBar() }) {
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
      modifier =
          Modifier.fillMaxSize()
              .padding(it)
              .verticalScroll(rememberScrollState())
              .testTag("IngredientsList"),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally) {
        if (ingredients.isEmpty() && potentialIngredients.isEmpty()) {
          Text(
              "No ingredients added yet",
              modifier = Modifier.fillMaxSize().padding(16.dp, 0.dp).testTag("NoIngredients"),
              color = MaterialTheme.colorScheme.secondary,
              fontSize = MaterialTheme.typography.headlineSmall.fontSize)
        } else {
          FlowRow(
              horizontalArrangement = Arrangement.Start, verticalArrangement = Arrangement.Top) {
                ingredients.forEach { ingredient ->
                  GradientButton(
                      text = "${ingredient.name} ${ingredient.quantity}${ingredient.unit}",
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

@Composable
private fun BottomBar() {
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
              onClick = { Log.v("Finished", "Clicked") }, // TODO: Finish adding ingredients
              modifier = Modifier.width(200.dp).testTag("DoneButton"),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)) {
                Text(text = "Done", fontSize = 24.sp)
              }
        }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigation: Navigation) {
  TopAppBar(
      title = {
        Text(
            "Ingredients",
            modifier = Modifier.testTag("IngredientTitle"),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize)
      },
      navigationIcon = {
        IconButton(
            onClick = { navigation.goBack() },
            content = {
              Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = "Back",
                  modifier = Modifier.testTag("BackButton"),
                  tint = PrimaryPurple)
            },
            modifier = Modifier.testTag("BackButton"))
      },
      modifier = Modifier.testTag("TopBar"))
}

// Comment out increases coverage...
// @Preview
// @Composable
// fun IngredientScreenPreview() {
//  IngredientScreen(Navigation(rememberNavController()), listOf(), listOf())
// }
