package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.ui.components.list.MealList
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.ui.components.selector.DateSelector
import com.github.se.polyfit.viewmodel.dailyRecap.DailyRecapViewModel

@Composable
fun DailyRecapScreen(
    navigateBack: () -> Unit,
    navigateTo: (String?) -> Unit,
    dailyRecapViewModel: DailyRecapViewModel = hiltViewModel(),
) {
  val context = LocalContext.current
  val meals by dailyRecapViewModel.meals.collectAsState()
  val isFetching by dailyRecapViewModel.isFetching.collectAsState()
  val noMeals = remember(meals) { meals.isEmpty() }
  val occasions = MealOccasion.entries.filter { it != MealOccasion.OTHER }
  val lifecycleOwner = LocalLifecycleOwner.current

  // This ensures that when we navigate back after edits, the screen is updated
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_RESUME) {
        dailyRecapViewModel.getMealsOnDate()
      }
    }

    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }

  Scaffold(topBar = { SimpleTopBar("Overview", navigateBack) }) {
    Column(modifier = Modifier.padding(it).testTag("DailyRecapScreen")) {
      DateSelector(onConfirm = dailyRecapViewModel::getMealsOnDate, title = "Date")
      if (isFetching) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize().padding(16.dp).testTag("Spinner"))
        return@Scaffold
      }

      if (noMeals) {
        Box(
            modifier = Modifier.fillMaxSize().testTag("NoMealsBox"),
            contentAlignment = Alignment.Center) {
              Text(
                  text = context.getString(R.string.no_recorded_meals),
                  modifier = Modifier.testTag("NoRecordedMeals"))
            }
        return@Scaffold
      }

      LazyColumn {
        items(occasions) { occasion ->
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
