package com.github.se.polyfit.model.post

import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.Nutrient

data class Post(
    val userId: String,
    val description: String,
    val images: List<Bitmap>,
    val location: Location,
    val meal: Meal
) {
  override fun toString(): String {
    return "The post from the user ${userId} with the following description ${description}" +
        "containts ${images.size} from the following location ${location}" +
        "the following meal ${meal}"
  }

  fun getCarbs(): Nutrient? {
    return meal.nutritionalInformation.getNutrient("carb")
  }

  fun getFat(): Nutrient? {
    return meal.nutritionalInformation.getNutrient("fat")
  }

  fun getProtein(): Nutrient? {
    return meal.nutritionalInformation.getNutrient("protein")
  }

  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

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
    // ToDo test this with the live database to make sure that it works properly
    fun serialize(data: Post): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["userId"] = data.userId
        this["description"] = data.description
        this["images"] = data.images
        this["location"] = data.location
        this["meal"] = data.meal.serialize()
      }
    }

    fun deserialize(data: Map<String, Any>): Post {
      return try {
        val userID = data["userId"] as String
        val description = data["description"] as String
        val images = data["images"] as List<Bitmap>
        val location = data["location"] as Location
        val meal = Meal.deserialize(data["meal"] as Map<String, Any>)

        return Post(userID, description, images, location, meal)
      } catch (e: Exception) {
        Log.e("Post", "Failed to deserialize Post object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Post object", e)
      }
    }

    fun default(): Post {
      return Post(
          "testId",
          "Description",
          mutableListOf(),
          Location("EFPL").apply {
            latitude = 46.5183
            longitude = 6.5665
          },
          Meal.default())
    }
  }
}
