package com.github.se.polyfit.ui.components.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.PurpleGrey40
import com.github.se.polyfit.ui.utils.titleCase

@Composable
fun MealOccasionSelector(
    startOccasion: MealOccasion,
    onConfirm: (MealOccasion) -> Unit,
    modifier: Modifier = Modifier
) {
  var mealOccasion by remember { mutableStateOf(startOccasion) }

  fun onClick(occasion: MealOccasion) {
    mealOccasion = occasion
    onConfirm(occasion)
  }

  Column(modifier = modifier.fillMaxWidth().testTag("MealOccasionSelector")) {
    Text(
        text = "Meal Occasion",
        style = MaterialTheme.typography.titleLarge,
        color = PurpleGrey40,
        modifier = Modifier.padding(16.dp, 0.dp).testTag("Title"))
    Row(
        modifier = Modifier.fillMaxWidth().testTag("ButtonRow"),
        horizontalArrangement = Arrangement.SpaceEvenly) {
          Column(modifier = Modifier.testTag("LeftColumn")) {
            OccasionRadioButton(MealOccasion.BREAKFAST, mealOccasion, ::onClick)
            OccasionRadioButton(MealOccasion.DINNER, mealOccasion, ::onClick)
          }
          Column(modifier = Modifier.testTag("RightColumn")) {
            OccasionRadioButton(MealOccasion.LUNCH, mealOccasion, ::onClick)
            OccasionRadioButton(MealOccasion.SNACK, mealOccasion, ::onClick)
          }
        }
  }
}

@Composable
private fun OccasionRadioButton(
    radioButtonOccasion: MealOccasion,
    currentOccasion: MealOccasion,
    onClick: (MealOccasion) -> Unit
) {
  val occasionName = titleCase(radioButtonOccasion.name)
  val selected = radioButtonOccasion == currentOccasion
  val colors =
      RadioButtonColors(
          selectedColor = PrimaryPurple,
          unselectedColor = PrimaryPurple,
          disabledSelectedColor = MaterialTheme.colorScheme.secondary,
          disabledUnselectedColor = MaterialTheme.colorScheme.secondary)

  Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.testTag("${occasionName}Row")) {
        RadioButton(
            selected = selected,
            colors = colors,
            onClick = { onClick(radioButtonOccasion) },
            modifier = Modifier.testTag("${occasionName}Button"))
        Text(
            text = titleCase(occasionName),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.testTag("${occasionName}Text"))
      }
}

@Preview
@Composable
fun Test() {
  MealOccasionSelector(MealOccasion.BREAKFAST, {})
}
