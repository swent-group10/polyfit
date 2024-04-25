package com.github.se.polyfit.ui.components.dialog

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.ingredients.IngredientNutritionEditFields
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.SecondaryGrey

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
        Nutrient("calories", 0.0, MeasurementUnit.CAL),
        Nutrient("totalWeight", 0.0, MeasurementUnit.G),
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
      val nutritionalInformation = NutritionalInformation(nutritionFields.toMutableList())
      val totalWeight = nutritionalInformation.getNutrient("totalWeight")

      Column(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(top = 20.dp, bottom = 20.dp)
                  .testTag("AddIngredientContentContainer")) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchIngredients(searchText = searchText, onSearchTextChanged = { searchText = it })

            Spacer(modifier = Modifier.height(16.dp))

            IngredientNutritionEditFields(nutritionFields = nutritionFields)

            PrimaryButton(
                onClick = {
                  onAddIngredient(
                      Ingredient(
                          name = searchText,
                          id = 0, // TODO: might need changing on how we handle the id
                          amount = totalWeight!!.amount,
                          unit = totalWeight.unit,
                          nutritionalInformation = nutritionalInformation))
                  onClickCloseDialog()
                },
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally),
                text = "Add",
                isEnabled = searchText.isNotBlank() && totalWeight!!.amount > 0.0)
          }
    }
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
