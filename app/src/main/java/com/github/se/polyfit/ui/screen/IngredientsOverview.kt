package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.theme.PolyfitTheme
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.getGradient
import com.github.se.polyfit.ui.theme.outlineLight

// TODO THIS CLASS IS TEMPORARY, REMOVE IT WHEN DOING THE VIEWMODEL
data class IngredientsTMP(
    val name: String,
    val servingSize: Int,
    val calories: Int,
    val carbs: Int,
    val fat: Int,
    val protein: Int
)

val i1 = IngredientsTMP("Apple", 100, 52, 14, 0, 0)
val i2 = IngredientsTMP("Banana", 100, 89, 23, 0, 1)
val i3 = IngredientsTMP("Carrot", 100, 41, 10, 0, 1)
val i4 = IngredientsTMP("Date", 100, 282, 75, 0, 2)
val i5 = IngredientsTMP("Eggplant", 100, 25, 6, 0, 1)

private val ListProducts = listOf(i1, i2, i3, i4, i5)

@Preview
@Composable
private fun PreviewIngredientsOverview() {
  PolyfitTheme(darkTheme = true, dynamicColor = false) { IngredientsOverview() }
}

@Composable
fun IngredientsOverview(
    navigateBack: () -> Unit = {},
    navigateForward: () -> Unit = {},
    onClickFloatingButton: () -> Unit = {}
) {

  Scaffold(
      topBar = { TopBar { navigateBack() } },
      bottomBar = { BottomBar(navigateForward = navigateForward) },
      floatingActionButton = { FloatingActionButton(onClickFloatingButton) },
      containerColor = MaterialTheme.colorScheme.background,
  ) {
    ListProducts(ListProducts = ListProducts, Modifier.fillMaxSize().padding(it))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigateBack: () -> Unit) {
  TopAppBar(
      title = {
        Text(
            "Product",
            modifier = Modifier.testTag("ProductTitle"),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize)
      },
      navigationIcon = {
        IconButton(
            onClick = { navigateBack() },
            content = {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  modifier = Modifier.testTag("BackButton"),
                  tint = MaterialTheme.colorScheme.outline)
            },
            colors =
                IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier.testTag("BackButton"))
      },
      modifier = Modifier.testTag("TopBar"),
      colors =
          TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background))
}

@Composable
private fun ListProducts(ListProducts: List<IngredientsTMP>, modifier: Modifier) {
  val gradient = getGradient(active = true)

  LazyColumn(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    items(ListProducts) {
      Card(
          modifier = Modifier.fillMaxWidth(0.8f).padding(16.dp, 8.dp),
          colors =
              CardDefaults.cardColors(
                  MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onSurface),
          border = BorderStroke(2.dp, gradient),
          elevation = CardDefaults.cardElevation(0.dp)) {
            Text(
                text = it.name,
                modifier = Modifier.padding(32.dp, 8.dp),
                color = MaterialTheme.colorScheme.outline,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize)

            TextIngredient(value = it.servingSize, text = "Serving Size", unit = "g")
            TextIngredient(it.calories, "Calories", "kcal")
            TextIngredient(it.carbs, "Carbs", "g")
            TextIngredient(it.fat, "Fat", "g")
            TextIngredient(it.protein, "Protein", "g")
          }
    }
  }
}

@Composable
private fun TextIngredient(value: Int, text: String, unit: String) {
  Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp, 2.dp),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = FontWeight.Light)

    Text(
        text = "$value $unit",
        modifier = Modifier.padding(16.dp, 2.dp),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = FontWeight.Light)
  }
}

@Composable
private fun FloatingActionButton(onClickFloatingButton: () -> Unit) {
  val shape = CircleShape
  FloatingActionButton(
      onClick = onClickFloatingButton,
      containerColor = MaterialTheme.colorScheme.background,
      shape = shape,
      elevation = FloatingActionButtonDefaults.elevation(0.dp),
      modifier =
          Modifier.border(BorderStroke(2.dp, getGradient(true)), shape).testTag("FloatingButton")) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Ingredient",
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.size(32.dp))
      }
}

@Composable
private fun BottomBar(navigateForward: () -> Unit) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .testTag("DoneBox")
              .background(MaterialTheme.colorScheme.surface)
              .padding(16.dp),
      contentAlignment = Alignment.Center) {
        PrimaryButton(
            text = "Generate\nRecipe",
            modifier = Modifier.align(Alignment.Center),
            onClick = navigateForward,
            color = MaterialTheme.colorScheme.outline)
      }
}

