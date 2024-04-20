package com.github.se.polyfit.ui.components.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@Composable
fun NutritionalInformation(mealViewModel: MealViewModel) {
  val meal = mealViewModel.meal.value!!
  val nutritionalInformation = meal.nutritionalInformation
  val nutrients = nutritionalInformation.nutrients
  val calories = nutritionalInformation.getNutrient("calories")

  LazyColumn(modifier = Modifier.padding(16.dp, 0.dp).testTag("NutritionalInformation")) {
    item { MealName(mealName = meal.name, onNameChange = { mealViewModel.setMealName(it) }) }

    // At the very least, a meal should always have calories
    if (calories == null) {
      item {
        Text(
            "No nutritional information available",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag("NoNutritionalInformation"))
      }
    } else {
      item { NutrientInfo(nutrient = calories, style = MaterialTheme.typography.bodyLarge) }
      items(nutrients) { nutrient ->
        if (nutrient != calories) {
          NutrientInfo(nutrient = nutrient)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealName(mealName: String, onNameChange: (String) -> Unit) {
  var textState by remember { mutableStateOf(mealName) }
  var isEditable by remember { mutableStateOf(false) }

  Column {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (isEditable) {
        TextField(
            value = textState,
            onValueChange = { newValue -> textState = newValue },
            modifier = Modifier.testTag("EditMealNameTextField").weight(1f),
            singleLine = true,
            colors =
                TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = PrimaryPurple,
                    cursorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                      onNameChange(textState)
                      isEditable = false
                    }))
      } else {
        Text(
            text = textState.ifEmpty { "Enter name here" },
            color = PrimaryPurple,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.testTag("MealName").align(Alignment.CenterVertically))
      }

      IconButton(onClick = { isEditable = true }, modifier = Modifier.testTag("EditMealButton")) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit meal name",
            tint = PrimaryPurple)
      }
    }
    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
  }

  LaunchedEffect(isEditable) {
    if (!isEditable) {
      onNameChange(textState)
    }
  }
}

@Composable
private fun NutrientInfo(
    nutrient: Nutrient,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth().testTag("NutrientInfo")) {
        Text(
            nutrient.getFormattedName(), style = style, modifier = Modifier.testTag("NutrientName"))
        Text(
            nutrient.getFormattedAmount(),
            style = style,
            modifier = Modifier.testTag("NutrientAmount"))
      }
}
