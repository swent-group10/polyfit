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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.components.list.MealList
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.ui.components.selector.DateSelector
import com.github.se.polyfit.viewmodel.dailyRecap.DailyRecapViewModel

@Composable
fun DailyRecapScreen(
    navigateBack: () -> Unit,
    navigateTo: () -> Unit,
    dailyRecapViewModel: DailyRecapViewModel = hiltViewModel()
) {
  val meals by dailyRecapViewModel.meals.collectAsState()
  val isFetching by dailyRecapViewModel.isFetching.collectAsState()
  val occasions = MealOccasion.entries.filter { it != MealOccasion.OTHER }

  Scaffold(topBar = { SimpleTopBar("Overview", navigateBack) }) {
    Column(modifier = Modifier.padding(it)) {
      DateSelector(onConfirm = dailyRecapViewModel::setDate, title = "Date")
      if (isFetching) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().padding(16.dp))
      }
      if (!isFetching) {
        LazyColumn {
          items(occasions.size) { index ->
            val occasion = occasions[index]
            MealList(
                meals = meals,
                occasion = occasion,
                navigateTo = navigateTo,
                modifier = Modifier.padding(0.dp, 8.dp))
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

//  val tags =
//      mutableListOf(
//          MealTag("This is a long tag name", MealTagColor.BABYPINK),
//          MealTag("And yet another long", MealTagColor.LAVENDER),
//          MealTag("And yet another long long", MealTagColor.BRIGHTORANGE))
//  val ingredient = Ingredient.default()
//  ingredient.nutritionalInformation.nutrients.add(Nutrient("calories", 10.0,
// MeasurementUnit.KCAL))
//  val meal1 = Meal.default()
//  meal1.name = "Burger"
//  meal1.occasion = MealOccasion.BREAKFAST
//  meal1.addIngredient(ingredient)
//  meal1.tags.addAll(tags)
//  val meal2 = Meal.default()
//  meal2.name = "Pie"
//  meal2.occasion = MealOccasion.BREAKFAST
//  meal2.addIngredient(ingredient)
//
//  val meal3 = Meal.default()
//  meal3.name = "Pizza"
//  meal3.occasion = MealOccasion.DINNER
//  meal3.addIngredient(ingredient)
//
//  val meal4 = Meal.default()
//  meal4.name = "Pasta"
//  meal4.occasion = MealOccasion.SNACK
//  meal4.addIngredient(ingredient)
//  val meals = mutableListOf(meal1, meal2, meal3, meal4)
