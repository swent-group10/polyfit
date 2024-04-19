package com.github.se.polyfit.model.post

import android.graphics.Bitmap
import android.location.Location
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient

data class Post(
    val description: String,
    val images: List<Bitmap>,
    val location: Location,
    val carbs: Nutrient,
    val fat: Nutrient,
    val protein: Nutrient,
    val meal: Meal
) {
  fun getIngredientCalories(): List<Pair<String, Nutrient>> {
    return meal.ingredients.mapNotNull { ingredient ->
      val nutrient =
          ingredient.nutritionalInformation.nutrients.firstOrNull { it.nutrientType == "calories" }
      if (nutrient != null) Pair(ingredient.name, nutrient) else null
    }
  }

  fun getIngredientWeight(): List<Pair<String, Double>> {
    return meal.ingredients.mapNotNull { ingredient -> Pair(ingredient.name, ingredient.amount) }
  }

  companion object {
    fun serialize(): Map<String, Any> {
      return mapOf("" to "")
    }

    fun deserialize(data: Map<String, Any>): Post {
      return default()
    }

    fun default(): Post {
      return Post(
          "Description",
          mutableListOf(),
          Location("EFPL").apply {
            latitude = 46.5183
            longitude = 6.5665
          },
          Nutrient("Carbs", 0.23, MeasurementUnit.G),
          Nutrient("Fat", 0.6, MeasurementUnit.G),
          Nutrient("Protein", 2.0, MeasurementUnit.G),
          Meal.default())
    }
  }
}
