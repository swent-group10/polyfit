package com.github.se.polyfit.data.api.Spoonacular

import android.util.Log
import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import org.json.JSONObject

data class RecipeNutritionResponseAPI(
    val status: APIResponse,
    val nutrients: List<Nutrient>,
    val ingredients: List<Ingredient>
) {
  companion object {
    private fun failure(): RecipeNutritionResponseAPI {
      return RecipeNutritionResponseAPI(
          status = APIResponse.FAILURE, nutrients = emptyList(), ingredients = emptyList())
    }

    fun fromJsonObject(jsonObject: JSONObject): RecipeNutritionResponseAPI {
      try {
        val nutrientsJsonObject = jsonObject.optJSONArray("nutrients") ?: return failure()
        val nutrients: List<Nutrient> =
            (0 until nutrientsJsonObject.length()).map {
              Nutrient(
                  nutrientType = nutrientsJsonObject.getJSONObject(it).getString("name"),
                  amount = nutrientsJsonObject.getJSONObject(it).getDouble("amount"),
                  unit =
                      MeasurementUnit.fromString(
                          nutrientsJsonObject.getJSONObject(it).getString("unit")))
            }

        val ingredientsJsonArray = jsonObject.optJSONArray("ingredients") ?: return failure()

        val ingredients =
            (0 until ingredientsJsonArray.length())
                .map {
                  val ingredientJsonArray = ingredientsJsonArray.getJSONObject(it)
                  val nutrientArray = ingredientJsonArray.getJSONArray("nutrients")

                  val nutrients =
                      (0 until nutrientArray.length())
                          .map {
                            val currentNutrient = nutrientArray.getJSONObject(it)
                            Nutrient(
                                nutrientType = currentNutrient.getString("name"),
                                amount = currentNutrient.getDouble("amount"),
                                unit =
                                    MeasurementUnit.fromString(currentNutrient.getString("unit")))
                          }
                          .toMutableList()

                  val nutritionalInformation = NutritionalInformation(nutrients)

                  Ingredient(
                      id = ingredientJsonArray.getLong("id"),
                      name = ingredientJsonArray.getString("name"),
                      nutritionalInformation = nutritionalInformation,
                      amount = ingredientJsonArray.getDouble("amount"),
                      unit = MeasurementUnit.fromString(ingredientJsonArray.getString("unit")))
                }
                .toList()

        return RecipeNutritionResponseAPI(APIResponse.SUCCESS, nutrients, ingredients)
      } catch (e: Exception) {
        Log.e("RecipeNutritionResponseAPI", "Error parsing JSON", e)
        return failure()
      }
    }
  }
}
