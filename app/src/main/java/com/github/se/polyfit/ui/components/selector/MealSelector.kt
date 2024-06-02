package com.github.se.polyfit.ui.components.selector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.roundTwoDecimal
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.textField.MealInputTextField
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.SecondaryGrey

@Composable
fun MealSelector(
    selectedMeal: Meal,
    setSelectedMeal: (Meal) -> Unit,
    meals: List<Meal>,
    setPostMeal: (Meal) -> Unit
) {
  val context = LocalContext.current
  var showMealSearch by remember { mutableStateOf(false) }
  val isDisplayMealDetails by
      remember(selectedMeal) { derivedStateOf { selectedMeal.isComplete() } }

  Column(modifier = Modifier.testTag("MealSelector")) {
    if (!showMealSearch) {
      Row(
          modifier =
              Modifier.padding(start = 12.dp, top = 20.dp)
                  .fillMaxWidth()
                  .testTag("MealSelectorRow")
                  .clickable {
                    showMealSearch = true
                    setSelectedMeal(Meal.default())
                  }) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star",
                tint = SecondaryGrey,
                modifier = Modifier.padding(end = 20.dp))

            if (!isDisplayMealDetails) {
              Text(
                  text = context.getString(R.string.shareRecipePlaceholder),
                  color = SecondaryGrey,
                  fontSize = 20.sp,
              )
            } else {
              Text(
                  text = selectedMeal.name,
                  fontSize = 20.sp,
                  modifier = Modifier.weight(1f),
              )
              Text(
                  text =
                      context.getString(
                          R.string.kcalvalue,
                          selectedMeal.calculateTotalCalories().roundTwoDecimal()),
                  fontSize = 20.sp,
                  modifier = Modifier.weight(1f).padding(end = 20.dp),
                  textAlign = TextAlign.End,
              )
            }
          }
    } else {
      MealInputTextField(
          meals = meals,
          onCollapseSearchBar = { showMealSearch = false },
          onMealChange = {
            setSelectedMeal(it)
            setPostMeal(it)
          })
    }
    if (isDisplayMealDetails) {
      MealDetails(selectedMeal, Modifier.padding(top = 10.dp))
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MealDetails(meal: Meal, modifier: Modifier = Modifier) {
  Box(modifier = modifier.fillMaxWidth().testTag("MealDetails")) {
    Column(modifier = Modifier.fillMaxWidth(0.75f).align(Alignment.Center)) {
      LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
        items(meal.ingredients) { ingredient ->
          Row(
              modifier = Modifier.fillMaxWidth().padding(4.dp).testTag("Ingredient"),
              verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ingredient.name,
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    fontSize = 16.sp,
                    color = SecondaryGrey)

                Text(
                    text = "${ingredient.amount} ${ingredient.unit.toString().lowercase()}",
                    modifier = Modifier.weight(1f).padding(end = 10.dp),
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    color = SecondaryGrey)
              }
          HorizontalDivider(color = Color.LightGray)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      FlowRow(
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier.align(Alignment.CenterHorizontally)) {
            val macros = meal.getMacros()
            val nutrients =
                listOf(
                    Pair("Carbs", macros["carbohydrates"]),
                    Pair("Fat", macros["fat"]),
                    Pair("Protein", macros["protein"]),
                )

            nutrients.forEach { (nutrientName, amount) ->
              GradientBox(
                  outerModifier = Modifier.padding(8.dp),
                  innerModifier = Modifier.size(80.dp),
                  round = 50.0) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                      Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          modifier = Modifier.testTag(nutrientName)) {
                            Text(text = "${amount!!.roundTwoDecimal()} g", color = SecondaryGrey)
                            Text(text = nutrientName, color = PrimaryPurple, fontSize = 15.sp)
                          }
                    }
                  }
            }
          }
    }
  }
}
