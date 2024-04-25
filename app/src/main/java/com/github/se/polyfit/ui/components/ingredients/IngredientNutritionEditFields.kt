package com.github.se.polyfit.ui.components.ingredients

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.SecondaryGrey
import com.github.se.polyfit.ui.utils.removeLeadingZerosAndNonDigits

@Composable
fun IngredientNutritionEditFields(
    modifier: Modifier = Modifier,
    nutritionFields: MutableList<Nutrient> = mutableListOf()
) {

  // TODO: add a way to enter more fields/type of nutritents

  LazyColumn(modifier = modifier.testTag("NutritionInfoContainer")) {
    itemsIndexed(nutritionFields) { index, nutrient ->
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
}
