package com.github.se.polyfit.ui.components.textField

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.ui.theme.SecondaryGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealInputTextField(
    meals: List<Meal>,
    onCollapseSearchBar: () -> Unit = {},
    onMealChange: (Meal) -> Unit = {}
) {
  val context = LocalContext.current
  var showIngredientSearch by remember { mutableStateOf(false) }
  var searchText by remember { mutableStateOf("") }
  var searchResults by remember { mutableStateOf(meals) }

  // Set the search bar to be at most 5 items tall, then scroll
  val maxHeight = (74 + minOf(5, searchResults.size) * 41).dp

  SearchBar(
      modifier =
          Modifier.padding(start = 20.dp, end = 20.dp)
              .fillMaxWidth()
              .heightIn(max = maxHeight)
              .testTag("SearchMealBar"),
      query = searchText,
      onQueryChange = {
        searchText = it
        showIngredientSearch = true
      },
      onSearch = {
        searchResults = meals.filter { meal -> meal.name.contains(searchText, ignoreCase = true) }
      },
      active = showIngredientSearch,
      onActiveChange = { showIngredientSearch = it },
      leadingIcon = {
        Icon(Icons.Filled.Search, contentDescription = "search", tint = SecondaryGrey)
      },
      placeholder = {
        Text(
            text = context.getString(R.string.enterMealPlaceholder),
            color = SecondaryGrey,
            style = TextStyle(fontSize = 17.sp),
            modifier = Modifier.testTag("Placeholder"))
      }) {
        LazyColumn(
            modifier = Modifier.testTag("MealSearchScrollableList"),
        ) {
          items(searchResults) { meal ->
            Row(
                modifier =
                    Modifier.fillMaxWidth().padding(8.dp).testTag("Meal").clickable {
                      onMealChange(meal)
                      onCollapseSearchBar()
                    },
                verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      text = meal.name,
                      modifier = Modifier.weight(1f).padding(start = 10.dp).testTag("MealName"),
                      fontSize = 16.sp,
                  )
                  Text(
                      text = meal.calculateTotalCalories().toString() + " kcal",
                      modifier = Modifier.weight(1f).padding(end = 10.dp).testTag("MealCalories"),
                      textAlign = TextAlign.End,
                      fontSize = 16.sp)
                }
            HorizontalDivider()
          }
        }
      }
}
