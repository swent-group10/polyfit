package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.button.GradientButton
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.dialog.AddIngredientDialog
import com.github.se.polyfit.ui.components.ingredients.IngredientList
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.viewmodel.meal.MealViewModel

/**
 * IngredientScreen is the screen where the user can add ingredients to the meal. The user can add
 * ingredients by clicking the add button and then entering the name and amount. The user can also
 * remove ingredients by clicking the remove button.
 *
 * @param mealViewModel The view model for the meal
 * @param navigateBack The function to navigate back
 * @param navigateForward The function to navigate forward
 */
@Composable
fun IngredientScreen(
    mealViewModel: MealViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateForward: () -> Unit
) {

  val showAddIngredDialog = remember { mutableStateOf(false) }
  val enabled = mealViewModel.meal.collectAsState().value.ingredients.isNotEmpty()
  var showCancelDialog by remember { mutableStateOf(false) }

  Scaffold(
      topBar = { SimpleTopBar(title = "Ingredients", navigateBack = { showCancelDialog = true }) },
      bottomBar = {
        BottomBar(
            onClickAddIngred = { showAddIngredDialog.value = true },
            navigateForward = navigateForward,
            enabled = enabled)
      }) {
        IngredientList(it, mealViewModel)
        if (showAddIngredDialog.value) {
          AddIngredientDialog(
              onClickCloseDialog = { showAddIngredDialog.value = false },
              onAddIngredient = mealViewModel::addIngredient)
        }
      }
  if (showCancelDialog)
      CancelAddMealDialog(closeDialog = { showCancelDialog = false }, navigateBack)
}

@Composable
private fun BottomBar(onClickAddIngred: () -> Unit, navigateForward: () -> Unit, enabled: Boolean) {
  Column(
      modifier =
          Modifier.background(MaterialTheme.colorScheme.background)
              .padding(0.dp, 16.dp, 0.dp, 32.dp)
              .testTag("BottomBar"),
  ) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp).testTag("AddIngredientBox"),
        contentAlignment = Alignment.CenterEnd) {
          GradientButton(
              onClick = {
                Log.v("Add Ingredient", "Clicked")
                onClickAddIngred()
              },
              active = true,
              round = true,
              icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Ingredient",
                    tint = MaterialTheme.colorScheme.primary)
              },
              modifier = Modifier.testTag("AddIngredientButton"))
        }
    Box(
        modifier = Modifier.fillMaxWidth().testTag("DoneBox"),
        contentAlignment = Alignment.Center) {
          PrimaryButton(
              onClick = {
                navigateForward()
                Log.v("Finished", "Clicked")
              },
              text = "Done",
              isEnabled = enabled,
              fontSize = 24,
              modifier = Modifier.width(200.dp).testTag("DoneButton"))
        }
  }
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

// @Composable
// fun IngredientScreenPreview() {
//  IngredientScreen(Navigation(rememberNavController()), listOf(), listOf())
// }
