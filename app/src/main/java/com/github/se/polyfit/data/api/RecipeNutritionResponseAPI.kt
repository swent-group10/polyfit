package com.github.se.polyfit.data.api

import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import org.json.JSONObject

data class RecipeNutritionResponseAPI(
    val nutrients: MutableList<Nutrient>,
    val ingredients: MutableList<Ingredient>
) {
  companion object {
    fun fromJsonObject(jsonObject: JSONObject): RecipeNutritionResponseAPI {
      val nutrientsJsonArray = jsonObject.getJSONArray("nutrients")
      val nutrients = mutableListOf<Nutrient>()

      for (i in 0 until nutrientsJsonArray.length()) {
        val nutrientJsonObject = nutrientsJsonArray.getJSONObject(i)
        val nutrient =
            Nutrient(
                nutrientType = nutrientJsonObject.getString("name"),
                amount = nutrientJsonObject.getDouble("amount"),
                unit = MeasurementUnit.fromString(nutrientJsonObject.getString("unit")),
            )
        nutrients.add(nutrient)
      }

      val ingredientsJsonArray = jsonObject.getJSONArray("ingredients")

      val ingredients = mutableListOf<Ingredient>()

      for (i in 0 until ingredientsJsonArray.length()) {
        val ingredientJsonObject = ingredientsJsonArray.getJSONObject(i)
        val nutritionalInformation = NutritionalInformation(mutableListOf())

        val nutrientsJSONArray = ingredientJsonObject.getJSONArray("nutrients")

        for (j in 0 until nutrientsJSONArray.length()) {
          val currNutrient = nutrientsJSONArray.getJSONObject(j)
          val newNutrient =
              Nutrient(
                  nutrientType = currNutrient.getString("name"),
                  amount = currNutrient.getDouble("amount"),
                  unit = MeasurementUnit.fromString(currNutrient.getString("unit")))

          nutritionalInformation.nutrients.add(newNutrient)
        }

        val newIngredient =
            Ingredient(
                name = ingredientJsonObject.getString("name"),
                id = ingredientJsonObject.getString("id").toInt(),
                amount = ingredientJsonObject.getString("amount").toDouble(),
                unit = MeasurementUnit.fromString(ingredientJsonObject.getString("unit")),
                nutritionalInformation = nutritionalInformation)

        ingredients.add(newIngredient)
      }

      return RecipeNutritionResponseAPI(nutrients = nutrients, ingredients = ingredients)
    }
  }
}
