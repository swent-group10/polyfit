package com.github.se.polyfit.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.components.button.GradientButton
import com.github.se.polyfit.ui.progressTracker.CalTracker
import com.github.se.polyfit.ui.theme.GhostPurpe
import com.github.se.polyfit.ui.theme.Purple40
import com.github.se.polyfit.ui.utils.OverviewTags

@Composable
fun MealTrackerCard(
    caloriesGoal: Int,
    meals: List<Pair<MealOccasion, Double>>,
    onCreateMealFromPhoto: () -> Unit,
    onCreateMealWithoutPhoto: () -> Unit,
    onViewRecap: () -> Unit,
) {
  Box(modifier = Modifier.fillMaxSize().testTag("OverviewMain")) {
    Column(modifier = Modifier.fillMaxSize().testTag("MealColumn")) {
      Spacer(modifier = Modifier.padding(8.dp))
      CalorieTracker(caloriesGoal, meals)

      MealDetails(meals)

      ActionButtons(onCreateMealFromPhoto, onCreateMealWithoutPhoto, onViewRecap)
    }
  }
}

@Composable
private fun CalorieTracker(caloriesGoal: Int, meals: List<Pair<MealOccasion, Double>>) {
  val totalCalories = meals.sumOf { it.second }
  CalTracker(
      progress = totalCalories.toFloat() / caloriesGoal,
      text = "$totalCalories/$caloriesGoal  kcal",
      color = Purple40,
      trackColor = GhostPurpe)
}

@Composable
private fun ActionButtons(
    onCreateMealFromPhoto: () -> Unit,
    onCreateMealWithoutPhoto: () -> Unit,
    onViewRecap: () -> Unit
) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround) {
        GradientButton(
            onClick = onCreateMealFromPhoto,
            modifier = Modifier.testTag(OverviewTags.overviewPictureBtn),
            active = true,
            icon = {
              Icon(
                  painterResource(R.drawable.outline_photo_camera),
                  contentDescription = "photoIcon",
                  tint = MaterialTheme.colorScheme.primary)
            })
        GradientButton(
            onClick = onCreateMealWithoutPhoto,
            modifier = Modifier.testTag(OverviewTags.overviewManualBtn),
            active = true,
            icon = {
              Icon(
                  painterResource(R.drawable.baseline_mode_edit_outline),
                  contentDescription = "penIcon",
                  tint = MaterialTheme.colorScheme.primary)
            })
        GradientButton(
            onClick = onViewRecap,
            modifier = Modifier.testTag(OverviewTags.overviewDetailsBtn),
            active = true,
            icon = {
              Icon(
                  painterResource(R.drawable.outline_assignment),
                  contentDescription = "historyIcon",
                  tint = MaterialTheme.colorScheme.primary)
            })
      }
}

@Composable
private fun MealDetails(meals: List<Pair<MealOccasion, Double>>) {
  Column(modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("CaloriePerMeal")) {
    meals.forEach { (meal, calories) -> MealDetailRow(meal, calories) }
  }
}

@Composable
private fun MealDetailRow(meal: MealOccasion, calories: Double) {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(
        text = meal.toLowerCaseString(),
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        color = Color.Black)
    Text(
        text = "${calories.toInt()} kcal",
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        color = Color.Black)
  }
}

@Preview
@Composable
fun MealTrackerCardPreview() {
  MealTrackerCard(
      caloriesGoal = 2000,
      meals =
          listOf(
              MealOccasion.BREAKFAST to 100.0,
              MealOccasion.LUNCH to 200.0,
              MealOccasion.DINNER to 300.0),
      onCreateMealFromPhoto = {},
      onCreateMealWithoutPhoto = {},
      onViewRecap = {})
}
