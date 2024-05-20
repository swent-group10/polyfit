package com.github.se.polyfit.ui.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Calories",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${recipeInfo.calories.toStringNoType()}",
                fontSize = MaterialTheme.typography.bodySmall.fontSize)
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Fat",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${recipeInfo.fat.toStringNoType()}",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize)
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Protein",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${recipeInfo.protein.toStringNoType()}",
                fontSize = MaterialTheme.typography.bodySmall.fontSize)
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Carbs",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${recipeInfo.carbs.toStringNoType()}",
                fontSize = MaterialTheme.typography.bodySmall.fontSize)
          }
        }
  }
}

@Composable
@Preview
fun preview() {
  NutriInfoCard(RecipeInformation.default())
}
