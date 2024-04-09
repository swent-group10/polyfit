package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.SecondaryGrey
import com.github.se.polyfit.ui.utils.removeLeadingZerosAndNonDigits

// Constants
// Add to list for new nutrition fields
val NUTRITION_LABELS = listOf("Serving Size", "Calories", "Carbs", "Fat", "Protein")
val NUTRITION_UNITS = listOf("g", "kcal", "g", "g", "g")
// Temporary list of available ingredient to search
val TMP_AVAILABLE_INGREDIENT = listOf("Apple", "Banana", "Carrot", "Date", "Eggplant", "apes")

@Composable
fun EditIngredientNutrition(
    // TODO: pass in viewmodel for editing specific ingredient's nutrition info
) {

  // TODO: currently the implementation hardcodes the field we want for quick editing the nutrition
  // info of an ingredient. This should be changed in the future depend on how we integrate the
  // ingredient info in a meal.
  val nutritionLabels = NUTRITION_LABELS
  val nutritionUnit = NUTRITION_UNITS
  val nutritionSize = remember { mutableStateListOf("0", "0", "0", "0", "0") }

  // TODO: when integrating with viewmodel, can be wrapped into a data class for cleaner code, for
  // now we are using indexing to access the three hardcoded list above.
  val nutritionLabelCount = nutritionLabels.size - 1
  (0..nutritionLabelCount).forEach { index ->
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier.testTag("NutritionInfoContainer " + nutritionLabels[index])
                .fillMaxWidth()
                .padding(start = 20.dp, end = 0.dp)) {
          Text(
              text = nutritionLabels[index],
              color = SecondaryGrey,
              style = TextStyle(fontSize = 18.sp),
              modifier = Modifier.testTag("NutritionLabel " + nutritionLabels[index]).weight(1.5f))

          TextField(
              value = nutritionSize[index],
              onValueChange = { newValue ->
                nutritionSize[index] = removeLeadingZerosAndNonDigits(newValue)
              },
              modifier =
                  Modifier.testTag("NutritionSizeInput " + nutritionLabels[index]).weight(0.5f),
              singleLine = true,
              colors =
                  TextFieldDefaults.colors(
                      focusedIndicatorColor = PrimaryPurple,
                      unfocusedIndicatorColor = SecondaryGrey,
                      focusedContainerColor = Color.Transparent,
                      unfocusedContainerColor = Color.Transparent))

          Text(
              text = nutritionUnit[index],
              style = TextStyle(fontSize = 18.sp),
              color = SecondaryGrey,
              modifier =
                  Modifier.testTag("NutritionUnit " + nutritionLabels[index])
                      .weight(0.4f)
                      .padding(start = 8.dp))
        }
    Spacer(modifier = Modifier.height(10.dp))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchIngredients() {
  var searchText by remember { mutableStateOf("") }

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
      onQueryChange = { searchText = it },
      onSearch = { Log.v("Search", "Searching for $searchText") },
      active = showIngredientSearch.value,
      onActiveChange = { showIngredientSearch.value = true },
      leadingIcon = {
        Icon(Icons.Filled.Search, contentDescription = "search", tint = SecondaryGrey)
      },
      placeholder = {
        Text(
            text = "Find an Ingredient...",
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
                          searchText = ingredient
                          showIngredientSearch.value = false
                          // TODO: Apply according nutrition facts to nutrition text fields
                        }
                        .padding(8.dp))
              HorizontalDivider()
          }
        }
      }
}

// @Composable
// fun AddButton(onClick: () -> Unit, modifier: Modifier) {
//  Button(
//      onClick = onClick,
//      modifier = modifier.padding(top = 16.dp).fillMaxWidth(0.5f),
//      shape = RoundedCornerShape(50.dp),
//      colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)) {
//        Text("Add", color = Color.White)
//      }
// }

@Composable
fun AddIngredientDialog(onClickCloseDialog: () -> Unit) {
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

            SearchIngredients()

            Spacer(modifier = Modifier.height(16.dp))

            EditIngredientNutrition()

            PrimaryPurpleButton(
                onClick = {
                  // TODO: Add ingredient to ingredient list
                  onClickCloseDialog()
                },
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally),
                text = "Add")
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