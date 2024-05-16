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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.screen.IngredientsTMP
import com.github.se.polyfit.ui.theme.getGradient

@Composable
fun ListProducts(ListProducts: List<IngredientsTMP>, modifier: Modifier) {
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