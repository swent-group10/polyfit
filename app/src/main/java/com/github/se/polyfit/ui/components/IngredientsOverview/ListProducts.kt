package com.github.se.polyfit.ui.components.IngredientsOverview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.screen.IngredientsTMP
import com.github.se.polyfit.ui.theme.getGradient

@Composable
fun ListProducts(listIngredients: List<IngredientsTMP>, modifier: Modifier) {
  val gradient = getGradient(active = true)
  val context = LocalContext.current

  LazyColumn(
      modifier = modifier.testTag("ListProductColumn"),
      horizontalAlignment = Alignment.CenterHorizontally) {
        items(listIngredients) { ingredient ->
          Card(
              modifier = Modifier.fillMaxWidth(0.8f).padding(16.dp, 8.dp).testTag("ProductCard"),
              colors =
                  CardDefaults.cardColors(
                      MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onSurface),
              border = BorderStroke(2.dp, gradient),
              elevation = CardDefaults.cardElevation(0.dp)) {
                Text(
                    text = ingredient.name,
                    modifier = Modifier.padding(32.dp, 8.dp).testTag("ProductName"),
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize)

                TextIngredient(
                    value = ingredient.servingSize,
                    text = "Serving Size",
                    unit = "g",
                    Modifier.testTag("ServingSize"))
                TextIngredient(
                    ingredient.calories,
                    context.getString(R.string.Calories),
                    "kcal",
                    Modifier.testTag("calories"))
                TextIngredient(
                    ingredient.carbs,
                    context.getString(R.string.Carbs),
                    "g",
                    Modifier.testTag("carbs"))
                TextIngredient(
                    ingredient.fat, context.getString(R.string.Fat), "g", Modifier.testTag("fat"))
                TextIngredient(
                    ingredient.protein,
                    context.getString(R.string.Protein),
                    "g",
                    Modifier.testTag("Protein"))
              }
        }
      }
}

@Composable
private fun TextIngredient(value: Int, text: String, unit: String, modifier: Modifier) {
  Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp, 2.dp).testTag("TextIngredient"),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = FontWeight.Light)

    Text(
        text = "$value $unit",
        modifier = Modifier.padding(16.dp, 2.dp).testTag("TextIngredientValue"),
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = FontWeight.Light)
  }
}
