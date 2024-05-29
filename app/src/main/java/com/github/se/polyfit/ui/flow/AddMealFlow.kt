package com.github.se.polyfit.ui.flow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.AdditionalMealInfoScreen
import com.github.se.polyfit.ui.screen.IngredientScreen
import com.github.se.polyfit.ui.screen.NutritionScreen
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@Composable
fun AddMealFlow(
    goBack: () -> Unit,
    goForward: () -> Unit,
    mealId: String? = null,
    mealViewModel: MealViewModel = hiltViewModel<MealViewModel>(),
) {
  val navController = rememberNavController()
  val navigation = Navigation(navController)
  mealViewModel.setMealData(mealId)
  var showCancelDialog by remember { mutableStateOf(false) }

  NavHost(navController = navController, startDestination = Route.Ingredients) {
    composable(Route.Ingredients) {
      IngredientScreen(
          mealViewModel = mealViewModel,
          navigateBack = { showCancelDialog = true },
          navigateForward = navigation::navigateToAdditionalMealInfo)
    }
    composable(Route.AdditionalMealInfo) {
      AdditionalMealInfoScreen(
          mealViewModel = mealViewModel,
          navigateBack = { navigation.goBack() },
          navigateForward = navigation::navigateToNutrition)
    }
    composable(Route.Nutrition) {
      NutritionScreen(
          mealViewModel = mealViewModel,
          navigateBack = { navigation.goBack() },
          navigateForward = { goForward() })
    }
  }
  if (showCancelDialog) CancelAddMealDialog(closeDialog = { showCancelDialog = false }, goBack)
}

@Composable
private fun CancelAddMealDialog(closeDialog: () -> Unit, goBack: () -> Unit) {
  val context = LocalContext.current

  Dialog(onDismissRequest = closeDialog) {
    GradientBox {
      Column(
          modifier = Modifier.fillMaxWidth().padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally) {
            Text(context.getString(R.string.confirmAddMealCancel))
            PrimaryButton(
                text = context.getString(R.string.confirmDiscard),
                onClick = goBack,
                modifier = Modifier.testTag("GoBack"))
            PrimaryButton(
                text = context.getString(R.string.denyRequest),
                color = PrimaryPink,
                onClick = closeDialog,
                modifier = Modifier.testTag("DenyButton"))
          }
    }
  }
}
