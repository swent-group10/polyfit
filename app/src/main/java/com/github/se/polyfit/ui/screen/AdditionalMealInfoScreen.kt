package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.model.meal.Meal

import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.ui.components.selector.DateSelector
import com.github.se.polyfit.ui.components.selector.MealOccasionSelector
import com.github.se.polyfit.ui.components.selector.MealTagSelector
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@Composable
fun AdditionalMealInfoScreen(
    mealViewModel: MealViewModel,
    navigateBack: () -> Unit,
    navigateForward: () -> Unit
) {
  val meal by mealViewModel.meal.collectAsState(initial = Meal.default())
  val occasion = meal.occasion
  val mealTags = meal.tags

  // Date will default to today, and tags are optional
  val isComplete = occasion != MealOccasion.OTHER

  Scaffold(
      topBar = { SimpleTopBar("Additional Meal Info", navigateBack) },
      bottomBar = { BottomBar(isComplete, navigateForward) }) {
        Column(
            modifier = Modifier.padding(it).fillMaxWidth().testTag("AdditionalMealInfoContainer")) {
              DateSelector(onConfirm = mealViewModel::setMealCreatedAt)
              MealOccasionSelector(occasion, mealViewModel::setMealOccasion)
              MealTagSelector(
                  mealTags = mealTags,
                  addMealTag = mealViewModel::addTag,
                  removeMealTag = mealViewModel::removeTag)
            }
      }
}

// TODO extract this and ingredient screen into a generic bottom bar component
@Composable
private fun BottomBar(isComplete: Boolean, navigateForward: () -> Unit) {
  BottomAppBar(
      modifier = Modifier.height(128.dp).testTag("BottomBar"), containerColor = Color.Transparent) {
        Box(
            modifier = Modifier.fillMaxWidth().testTag("DoneBox"),
            contentAlignment = Alignment.Center) {
              //              Button(
              //                  onClick = {
              //                    navigateForward()
              //                    Log.v("Additional Meal Information", "Done")
              //                  },
              //                  enabled = isComplete,
              //                  modifier = Modifier.width(200.dp).testTag("DoneButton"),
              //                  colors = ButtonDefaults.buttonColors(containerColor =
              // PrimaryPurple)) {
              //                    Text(text = "Done", fontSize = 24.sp)
              //                  }
              PrimaryButton(
                  onClick = {
                    navigateForward()
                    Log.v("Additional Meal Information", "Done")
                  },
                  modifier = Modifier.width(200.dp).testTag("DoneButton"),
                  text = "Done",
                  fontSize = 24,
                  isEnabled = isComplete)
            }
      }
}
