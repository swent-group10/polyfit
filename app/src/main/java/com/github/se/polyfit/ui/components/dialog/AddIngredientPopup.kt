package com.github.se.polyfit.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.ingredients.IngredientNutritionEditFields
import com.github.se.polyfit.ui.theme.PrimaryPink

/**
 * AddIngredientDialog is a composable function that creates a popup dialog for the user to add an
 * ingredient.
 *
 * @param onClickCloseDialog a callback function that closes the dialog
 * @param onAddIngredient a callback function that adds the ingredient to the meal
 */
@Composable
fun AddIngredientDialog(
    onClickCloseDialog: () -> Unit = {},
    onAddIngredient: (Ingredient) -> Unit = {}
) {
  // TODO: currently the implementation hardcodes the field we want for quick editing the nutrition
  // info of an ingredient. This should be changed in the future depend on how we integrate the
  // ingredient info in a meal.
  val nutritionFields = remember {
    mutableStateListOf(
        Nutrient("Calories", 0.0, MeasurementUnit.CAL),
        Nutrient("Total Weight", 0.0, MeasurementUnit.G),
        Nutrient("Carbohydrates", 0.0, MeasurementUnit.G),
        Nutrient("Fat", 0.0, MeasurementUnit.G),
        Nutrient("Protein", 0.0, MeasurementUnit.G))
  }

  var title by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onClickCloseDialog) {
    GradientBox(
        outerModifier = Modifier.testTag("AddIngredientPopupContainer"),
        innerModifier = Modifier.fillMaxWidth(),
        iconOnClick = onClickCloseDialog,
        icon = Icons.Filled.Close,
        iconColor = PrimaryPink,
        iconDescriptor = "close",
    ) {
      val nutritionalInformation = NutritionalInformation(nutritionFields.toMutableList())
      val totalWeight = nutritionalInformation.getNutrient("Total Weight")

      Column(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(top = 20.dp, bottom = 20.dp)
                  .testTag("AddIngredientContentContainer")) {
            Spacer(modifier = Modifier.height(16.dp))

            IngredientTitle(title = title, onTitleChanged = { title = it })

            Spacer(modifier = Modifier.height(16.dp))

            IngredientNutritionEditFields(nutritionFields = nutritionFields)

            PrimaryButton(
                onClick = {
                  onAddIngredient(
                      Ingredient(
                          name = title,
                          id = 0, // TODO: might need changing on how we handle the id
                          amount = totalWeight!!.amount,
                          unit = totalWeight.unit,
                          nutritionalInformation = nutritionalInformation))
                  onClickCloseDialog()
                },
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally),
                text = "Add",
                isEnabled = title.isNotBlank() && totalWeight!!.amount > 0.0)
          }
    }
  }
}

/**
 * IngredientTitle is a composable function that creates a text field for the user to enter the
 * ingredient name.
 *
 * @param title the title of the ingredient
 * @param onTitleChanged a callback function that updates the title
 */
@Composable
fun IngredientTitle(title: String, onTitleChanged: (String) -> Unit) {
  TextField(
      value = title,
      onValueChange = onTitleChanged,
      placeholder = { Text("Enter an Ingredient...") },
      modifier = Modifier.fillMaxWidth().testTag("EnterIngredientName"))
}

// Function for previewing the popup page
// @Preview
// @Composable
// fun TestPopupWindow() {
//  AddIngredientDialog(onClickCloseDialog = { })
// }
