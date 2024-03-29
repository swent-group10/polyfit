package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.theme.*

// Check if the input is not just a single "0", and if it has leading zeros
fun removeLeadingZeros(input: String): String {
  return if (input != "0" && input.startsWith("0")) {
    input.trimStart('0').ifEmpty { "0" }
  } else {
    input
  }
}

@Composable
fun NutritionFacts() {

  // TODO: Integrate backend data fields here or pass in nutrition viewmodel
  val nutritionLabels = listOf("Serving Size", "Calories", "Carbs", "Fat", "Protein")
  val nutritionSize = remember { mutableStateListOf("0", "0", "0", "0", "0") }
  val nutritionUnit = listOf("g", "kcal", "g", "g", "g")

  val nutritionLabelCount = nutritionLabels.size - 1

  (0..nutritionLabelCount).forEach { index ->
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 0.dp)) {
          Text(
              text = nutritionLabels[index],
              color = SecondaryGrey,
              style = TextStyle(fontSize = 18.sp),
              modifier = Modifier.weight(1.5f))

          TextField(
              value = nutritionSize[index],
              onValueChange = { newValue -> nutritionSize[index] = removeLeadingZeros(newValue) },
              modifier = Modifier.weight(0.5f),
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
              modifier = Modifier.weight(0.4f).padding(start = 8.dp))
        }
    Spacer(modifier = Modifier.height(10.dp))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchIngredients() {
  val searchText = remember { mutableStateOf("") }
  val showIngredientSearch = remember { mutableStateOf(false) }

  // TODO: change list of ingredients to search for here
  val ingredientDatabase = listOf("Apple", "Banana", "Carrot", "Date", "Eggplant", "apes")

  // TODO: (Optional) filter ingredients here or at backend
  val filteredIngredients =
      remember(searchText) {
        ingredientDatabase.filter { it.contains(searchText.value, ignoreCase = true) }
      }

  SearchBar(
      modifier = Modifier.padding(start = 20.dp, end = 20.dp),
      query = searchText.value,
      onQueryChange = { it -> searchText.value = it },
      onSearch = {},
      active = showIngredientSearch.value,
      onActiveChange = { showIngredientSearch.value = true },
      placeholder = {
        Row {
          Icon(Icons.Filled.Search, contentDescription = "search", tint = SecondaryGrey)
          Text(
              text = " Find an Ingredient...",
              color = SecondaryGrey,
              style = TextStyle(fontSize = 17.sp))
        }
      }) {
        LazyColumn {
          items(filteredIngredients) { ingredient ->
            Text(
                text = ingredient,
                modifier =
                    Modifier.fillMaxWidth()
                        .clickable {
                          searchText.value = ingredient
                          showIngredientSearch.value = false
                          // TODO: Apply according nutrition facts to nutrition text fields
                        }
                        .padding(8.dp))
            Divider()
          }
        }
      }
}

@Composable
fun AddButton(onClick: () -> Unit, modifier: Modifier) {
  Button(
      onClick = onClick,
      modifier = modifier.padding(top = 16.dp).fillMaxWidth(0.5f),
      shape = RoundedCornerShape(50.dp),
      colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)) {
        Text("Add", color = Color.White)
      }
}

@Composable
fun AddIngredientDialog(onClickCloseDialog: () -> Unit) {
  Dialog(onDismissRequest = onClickCloseDialog) {
    GradientBox(
        innerModifier = Modifier.fillMaxWidth(),
        iconOnClick = onClickCloseDialog,
        icon = Icons.Filled.Close,
        iconColor = PrimaryPink,
        iconDescriptor = "close",
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 20.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchIngredients()

        Spacer(modifier = Modifier.height(16.dp))

        NutritionFacts()

        AddButton(
            onClick = {
              // TODO: Add ingredient to ingredient list
              onClickCloseDialog()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally))
      }
    }
  }
}

// Temporary function for testing purpose
@Composable
@Preview
fun TestPopupWindow() {
  val showAddIngredDialog = remember { mutableStateOf(true) }

  Button(onClick = { showAddIngredDialog.value = true }) { Text("Show Pop-Up") }

  if (showAddIngredDialog.value) {
    AddIngredientDialog(onClickCloseDialog = { showAddIngredDialog.value = false })
  }
}
