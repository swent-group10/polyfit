package com.github.se.polyfit.data.api

import android.util.Log
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

data class ImageAnalysisResponseAPI(
    val status: APIResponse,
    val nutrition: List<Nutrient>,
    val category: String,
    val recipes: List<Int>
) {
  companion object {
    fun fromJsonObject(jsonObject: JSONObject): ImageAnalysisResponseAPI? {
      return try {
        val nutritionObject = jsonObject.optJSONObject("nutrition") ?: return null
        val nutrients = parseNutrients(nutritionObject)
        val category = jsonObject.optJSONObject("category")?.optString("name") ?: return null
        val recipesArray = jsonObject.optJSONArray("recipes") ?: return null
        val recipes = parseRecipes(recipesArray)
        val status = APIResponse.fromString(jsonObject.optString("status"))
        if (status == APIResponse.FAILURE) return null

        ImageAnalysisResponseAPI(
            status = status!!, nutrition = nutrients, category = category, recipes = recipes)
      } catch (e: JSONException) {
        Log.e("ImageAnalysisResponseAPI", "Error parsing JSON", e)
        null
      }
    }

    private fun parseNutrients(nutritionObject: JSONObject): List<Nutrient> {
      return nutritionObject
          .keys()
          .asSequence()
          .filter { it != "recipesUsed" }
          .map { key ->
            Nutrient(
                nutrientType = key,
                amount = nutritionObject.getJSONObject(key).getDouble("value"),
                unit =
                    MeasurementUnit.fromString(
                        nutritionObject.getJSONObject(key).getString("unit")))
          }
          .toList()
    }

    private fun parseRecipes(recipesArray: JSONArray): List<Int> {
      return (0 until recipesArray.length()).map { recipesArray.getJSONObject(it).getInt("id") }
    }
  }
}
