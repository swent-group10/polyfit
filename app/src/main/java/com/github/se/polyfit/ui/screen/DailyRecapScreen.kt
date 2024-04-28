package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.components.list.MealList
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.ui.components.selector.DateSelector
import com.github.se.polyfit.viewmodel.dailyRecap.DailyRecapViewModel
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@Composable
fun DailyRecapScreen(
    navigateBack: () -> Unit,
    navigateTo: () -> Unit,
    dailyRecapViewModel: DailyRecapViewModel = hiltViewModel(),
    mealViewModel: MealViewModel = hiltViewModel() // TODO: Remove when stop reusing mealviewmodel
) {
  val meals by dailyRecapViewModel.meals.collectAsState()
  val isFetching by dailyRecapViewModel.isFetching.collectAsState()
  val occasions = MealOccasion.entries.filter { it != MealOccasion.OTHER }

  Scaffold(topBar = { SimpleTopBar("Overview", navigateBack) }) {
    Column(modifier = Modifier.padding(it).testTag("DailyRecapScreen")) {
      DateSelector(onConfirm = dailyRecapViewModel::getMealsOnDate, title = "Date")
      if (isFetching) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize().padding(16.dp).testTag("Spinner"))
      }
      if (!isFetching) {
        LazyColumn {
          items(occasions.size) { index ->
            val occasion = occasions[index]
            MealList(
                meals = meals,
                occasion = occasion,
                navigateTo = navigateTo,
                modifier = Modifier.padding(0.dp, 8.dp),
                mealViewModel = mealViewModel)
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun DailyRecapScreenPreview() {
  DailyRecapScreen(navigateBack = {}, navigateTo = {})
}
