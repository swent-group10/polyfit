package com.github.se.polyfit.data.api.Spoonacular

import android.util.Log
import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import org.json.JSONException
import org.json.JSONObject

data class ImageAnalysisResponseAPI(
    val status: APIResponse,
    val nutrition: List<Nutrient>,
    val category: String,
    val recipes: List<Int>
) {
  companion object {
    private fun failure(): ImageAnalysisResponseAPI {
      return ImageAnalysisResponseAPI(
          status = APIResponse.FAILURE,
          nutrition = emptyList(),
          category = "",
          recipes = emptyList())
    }

    fun fromJsonObject(jsonObject: JSONObject): ImageAnalysisResponseAPI {
      return try {
        val nutritionObject = jsonObject.optJSONObject("nutrition") ?: return failure()

        val nutrients =
            nutritionObject
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

        val category = jsonObject.optJSONObject("category")?.optString("name") ?: return failure()

        val recipesArray = jsonObject.optJSONArray("recipes") ?: return failure()
        val recipes =
            (0 until recipesArray.length()).map { recipesArray.getJSONObject(it).getInt("id") }

        val status = APIResponse.fromString(jsonObject.optString("status"))
        if (status == APIResponse.FAILURE) return failure()

        ImageAnalysisResponseAPI(
            status = status, nutrition = nutrients, category = category, recipes = recipes)
      } catch (e: JSONException) {
        Log.e("ImageAnalysisResponseAPI", "Error parsing JSON", e)
        failure()
      }
    }
  }
}
