package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.ui.theme.*
import com.github.se.polyfit.ui.utils.removeLeadingZerosAndNonDigits

// Constants
// Temporary list of available ingredient to search
val TMP_AVAILABLE_INGREDIENT = listOf("Apple", "Banana", "Carrot", "Date", "Eggplant", "Apes")

@Composable
fun AddIngredientDialog(
    onClickCloseDialog: () -> Unit = {},
    onAddIngredient: (Ingredient) -> Unit = {}
) {
  // TODO: currently the implementation hardcodes the field we want for quick editing the nutrition
  // info of an ingredient. This should be changed in the future depend on how we integrate the
  // ingredient info in a meal.

  val nutritionFields = remember {
    mutableStateListOf<Nutrient>(
        Nutrient("totalWeight", 0.0, MeasurementUnit.G),
        Nutrient("calories", 0.0, MeasurementUnit.CAL),
        Nutrient("carbohydrates", 0.0, MeasurementUnit.G),
        Nutrient("fat", 0.0, MeasurementUnit.G),
        Nutrient("protein", 0.0, MeasurementUnit.G))
  }

  var searchText by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onClickCloseDialog) {
    GradientBox(
        outerModifier = Modifier.testTag("AddIngredientPopupContainer"),
        innerModifier = Modifier.fillMaxWidth(),
        iconOnClick = onClickCloseDialog,
        icon = Icons.Filled.Close,
        iconColor = PrimaryPink,
        iconDescriptor = "close",
    ) {
      Column(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(top = 20.dp, bottom = 20.dp)
                  .testTag("AddIngredientContentContainer")) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchIngredients(searchText = searchText, onSearchTextChanged = { searchText = it })

            Spacer(modifier = Modifier.height(16.dp))

            EditIngredientNutrition(nutritionFields)

            PrimaryPurpleButton(
                onClick = {
                  onAddIngredient(
                      Ingredient(
                          name = searchText,
                          id = 0, // TODO: might need changing on how we handle the id
                          amount = nutritionFields[0].amount,
                          unit = nutritionFields[0].unit,
                          nutritionalInformation =
                              com.github.se.polyfit.model.nutritionalInformation
                                  .NutritionalInformation(nutritionFields.toMutableList())))
                  onClickCloseDialog()
                },
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally),
                text = "Add",
                isEnabled = searchText.isNotBlank() && nutritionFields[0].amount > 0.0)
          }
    }
  }
}

@Composable
fun EditIngredientNutrition(nutritionFields: MutableList<Nutrient>) {

  nutritionFields.forEachIndexed { index, nutrient ->
    var isFocused by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(nutrient.amount.toString()) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier.testTag("NutritionInfoContainer " + nutrient.getFormattedName())
                .fillMaxWidth()
                .padding(start = 20.dp, end = 0.dp)) {
          Text(
              text = nutrient.getFormattedName(),
              color = SecondaryGrey,
              style = TextStyle(fontSize = 18.sp),
              modifier =
                  Modifier.testTag("NutritionLabel " + nutrient.getFormattedName()).weight(1.5f))

          TextField(
              keyboardOptions =
                  KeyboardOptions(
                      keyboardType = KeyboardType.Decimal,
                      imeAction = ImeAction.Done,
                  ),
              keyboardActions =
                  KeyboardActions(
                      onDone = {
                        try {
                          text = removeLeadingZerosAndNonDigits(text)
                          nutritionFields[index] = nutrient.copy(amount = text.toDouble())
                          text = text.toDouble().toString()
                        } catch (e: NumberFormatException) {}
                      }),
              value = text,
              onValueChange = { newValue -> text = newValue },
              modifier =
                  Modifier.testTag("NutritionSizeInput " + nutrient.getFormattedName())
                      .weight(0.5f)
                      .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (!isFocused) { // When the TextField loses focus
                          try {
                            text = removeLeadingZerosAndNonDigits(text)
                            nutritionFields[index] = nutrient.copy(amount = text.toDouble())
                            text = text.toDouble().toString()
                          } catch (e: NumberFormatException) {}
                        }
                      },
              singleLine = true,
              colors =
                  TextFieldDefaults.colors(
                      focusedIndicatorColor = PrimaryPurple,
                      unfocusedIndicatorColor = SecondaryGrey,
                      focusedContainerColor = Color.Transparent,
                      unfocusedContainerColor = Color.Transparent))

          // TODO: in the future, make it possible to choose between different units
          Text(
              text = nutrient.unit.toString().lowercase(),
              style = TextStyle(fontSize = 18.sp),
              color = SecondaryGrey,
              modifier =
                  Modifier.testTag("NutritionUnit " + nutrient.getFormattedName())
                      .weight(0.4f)
                      .padding(start = 8.dp))
        }
    Spacer(modifier = Modifier.height(10.dp))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchIngredients(searchText: String = "", onSearchTextChanged: (String) -> Unit = {}) {
  val showIngredientSearch = remember { mutableStateOf(false) }

  // TODO: change list of ingredients to search for here
  val ingredientDatabase = TMP_AVAILABLE_INGREDIENT

  // TODO: (Optional) filter ingredients here or at backend
  val filteredIngredients =
      remember(searchText) {
        ingredientDatabase.filter { it.contains(searchText, ignoreCase = true) }
      }

  SearchBar(
      modifier = Modifier.padding(start = 20.dp, end = 20.dp).testTag("SearchIngredientBar"),
      query = searchText,
      onQueryChange = onSearchTextChanged,
      onSearch = {
        Log.v("Search", "Searching for $searchText")
        showIngredientSearch.value = false
      },
      active = showIngredientSearch.value,
      onActiveChange = {},
      trailingIcon = {
        IconButton(
            modifier = Modifier.testTag("SearchIcon"),
            onClick = { showIngredientSearch.value = true }) {
              Icon(Icons.Filled.Search, contentDescription = "search", tint = SecondaryGrey)
            }
      },
      placeholder = {
        Text(
            text = "Enter an Ingredient...",
            color = SecondaryGrey,
            style = TextStyle(fontSize = 17.sp))
      }) {
        LazyColumn(modifier = Modifier.testTag("IngredientSearchScrollableList")) {
          items(filteredIngredients) { ingredient ->
            Text(
                text = ingredient,
                modifier =
                    Modifier.testTag("SearchResult $ingredient")
                        .fillMaxWidth()
                        .clickable {
                          onSearchTextChanged(ingredient)
                          showIngredientSearch.value = false
                          // TODO: Apply according nutrition facts to nutrition text fields
                        }
                        .padding(8.dp))
            Divider()
          }
        }
      }
}

// Function for previewing the popup page
// @Composable
// @Preview
// fun TestPopupWindow() {
//    AddIngredientDialog(onClickCloseDialog = { })
// }
