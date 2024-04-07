package com.github.se.polyfit.data.api

import android.util.Log
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import org.json.JSONObject

data class ImageAnalysisResponseAPI(
    val status: APIReponse,
    val nutrition: List<Nutrient>,
    val category: String,
    val recipes: List<Int>
) {
    companion object {
        fun fromJsonObject(jsonObject: JSONObject): ImageAnalysisResponseAPI {
            Log.d(
                "ImageAnalysisResponse", "response : ${jsonObject}"
            )
            val nutritionObject = jsonObject.getJSONObject("nutrition")
            // Iterate over all elements
            val nutrients = mutableListOf<Nutrient>()
            //iterage over all nutrients

            for (key in nutritionObject.keys().asSequence().filter { it != "recipesUsed" }) {

                val nutrient = Nutrient(
                    nutrientType = key,
                    amount = nutritionObject.getJSONObject(key).getDouble("value"),
                    unit = MeasurementUnit.fromString(
                        nutritionObject.getJSONObject(key).getString("unit")
                    )
                )
                nutrients.add(nutrient)
            }

            val category = jsonObject.getJSONObject("category").getString("name")

            val recipesArray = jsonObject.getJSONArray("recipes")
            val recipes = mutableListOf<Int>()
            for (i in 0 until recipesArray.length()) {
                recipes.add(recipesArray.getJSONObject(i).getInt("id"))
            }

            return ImageAnalysisResponseAPI(
                status = APIReponse.fromString(jsonObject.getString("status")),
                nutrition = nutrients,
                category = category,
                recipes = recipes
            )
        }
    }
}
