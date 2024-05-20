package com.github.se.polyfit.ui.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.recipe.RecipeInformation

@Composable
fun NutriInfoCard(recipeInfo: RecipeInformation) {
  Card(
      modifier = Modifier.padding(16.dp).fillMaxWidth(),
      shape = MaterialTheme.shapes.medium,
  ) {
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {
          NutriInfoColumn(title = "Calories", value = recipeInfo.calories.toStringNoType())
          NutriInfoColumn(title = "Fat", value = recipeInfo.fat.toStringNoType())
          NutriInfoColumn(title = "Protein", value = recipeInfo.protein.toStringNoType())
          NutriInfoColumn(title = "Carbs", value = recipeInfo.carbs.toStringNoType())
        }
  }
}

@Composable
fun NutriInfoColumn(title: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = value, fontSize = MaterialTheme.typography.bodySmall.fontSize)
  }
}
