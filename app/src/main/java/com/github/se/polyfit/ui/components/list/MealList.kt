package com.github.se.polyfit.ui.components.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.ui.theme.BorderPurple
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PurpleGrey40

@Composable
fun MealList(
    meals: List<Meal>,
    occasion: MealOccasion,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
  val filteredMeals = meals.filter { it.occasion == occasion }
  val totalCalories = filteredMeals.sumOf { it.calculateTotalCalories() }.toString()

  Surface(
      modifier = modifier.fillMaxWidth().padding(8.dp, 0.dp).testTag("MealList"),
      border = BorderStroke(1.dp, BorderPurple),
      shape = RoundedCornerShape(4.dp),
      color = Color.White) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    occasion.toLowerCaseString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = PurpleGrey40,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("OccasionTitle"))
                Text(
                    totalCalories,
                    style = MaterialTheme.typography.titleLarge,
                    color = PurpleGrey40,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("TotalCalories"))
              }
          HorizontalDivider(color = PrimaryPink)
          filteredMeals.forEach { meal -> MealCard(meal) { navigateTo(meal.id) } }
        }
      }
}

@Composable
private fun MealCard(meal: Meal, editMeal: () -> Unit) {
  Surface(color = Color.White, modifier = Modifier.clickable { editMeal() }.testTag("MealCard")) {
    Column {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            meal.name,
            style = MaterialTheme.typography.bodyLarge,
            color = PurpleGrey40,
            modifier = Modifier.testTag("MealName"))
        Text(
            "${meal.calculateTotalCalories()} ${MeasurementUnit.KCAL.name.lowercase()}",
            style = MaterialTheme.typography.bodyMedium,
            color = PurpleGrey40,
            modifier = Modifier.testTag("MealCalories"))
      }
      MealTagList(
          mealTags = meal.tags,
          addNewTag = {},
          editTag = {},
          displayOnly = true,
          modifier = Modifier.testTag("MealTagList"))
    }
  }
}
