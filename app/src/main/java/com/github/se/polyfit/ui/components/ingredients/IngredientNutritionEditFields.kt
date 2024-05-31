package com.github.se.polyfit.ui.components.ingredients

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.SecondaryGrey
import com.github.se.polyfit.ui.utils.removeLeadingZerosAndNonDigits

private const val MAX_NUTRITION_AMOUNT = 200_000
@Composable
fun IngredientNutritionEditFields(
    modifier: Modifier = Modifier,
    nutritionFields: MutableList<Nutrient> = mutableListOf()
) {

  // TODO: add a way to enter more fields/type of nutritents
  LazyColumn(modifier = modifier.testTag("NutritionInfoContainer")) {
    itemsIndexed(nutritionFields) { index, nutrient ->
      var text by remember {
        mutableStateOf(if (0 < nutrient.amount && nutrient.amount <= MAX_NUTRITION_AMOUNT) nutrient.amount.toString() else "")
      }
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
                value = text,
                placeholder = { Text(text = "0.0") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                onValueChange = {
                  val update = it.toDoubleOrNull()
                  text =
                      when {
                        update.isNotNull() && !(0 < update!! && update < MAX_NUTRITION_AMOUNT)-> ""
                        update.isNotNull() -> update.toString()
                        else -> text
                      }
                  updateNutrient(index, nutritionFields, nutrient, text)
                },
                modifier =
                    Modifier.testTag("NutritionSizeInput " + nutrient.getFormattedName())
                        .weight(0.5f),
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

private fun updateNutrient(
    index: Int,
    nutritionFields: MutableList<Nutrient>,
    nutrient: Nutrient,
    text: String,
) {
  var newText = text.ifEmpty { "0.0" }
  try {
    newText = removeLeadingZerosAndNonDigits(newText)
    nutritionFields[index] = nutrient.copy(amount = newText.toDouble())
  } catch (_: NumberFormatException) {
    Log.w("Catch Exception", "Text formatting gone wrong")
  }
}
