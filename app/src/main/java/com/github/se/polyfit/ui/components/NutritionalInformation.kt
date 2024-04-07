package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.ui.theme.PrimaryPurple

@Composable
fun NutritionalInformation(meal: Meal) {
  val nutritionalInformation = meal.nutritionalInformation
  val nutrients = nutritionalInformation.nutrients
  val calories = nutritionalInformation.getNutrient("calories")

  LazyColumn(modifier = Modifier.padding(16.dp, 0.dp).testTag("NutritionalInformation")) {
    item { MealName(meal.name) }

    // At the very least, a meal should always have calories
    if (calories == null) {
      item {
        Text(
            "No nutritional information available",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag("NoNutritionalInformation"))
      }
    } else {
      item { NutrientInfo(nutrient = calories, style = MaterialTheme.typography.bodyLarge) }
      nutrients.forEach { nutrient ->
        if (nutrient.nutrientType != "calories") {
          item { NutrientInfo(nutrient = nutrient) }
        }
      }
    }
  }
}

@Composable
private fun MealName(mealName: String) {
  Column {
    Text(
        mealName,
        color = PrimaryPurple,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.testTag("MealName"))
    HorizontalDivider()
  }
}

@Composable
private fun NutrientInfo(
    nutrient: Nutrient,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth().testTag("NutrientInfo")) {
        Text(
            nutrient.getFormattedName(), style = style, modifier = Modifier.testTag("NutrientName"))
        Text(
            nutrient.getFormattedAmount(),
            style = style,
            modifier = Modifier.testTag("NutrientAmount"))
      }
}
