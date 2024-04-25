package com.github.se.polyfit.model.post

import android.util.Log
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import java.time.LocalDate

interface UnmodifiablePost {
  val userId: String
  val description: String
  val location: Location
  val meal: Meal
  val createdAt: LocalDate
}

data class Post(
    override var userId: String,
    override var description: String,
    override var location: Location,
    override var meal: Meal,
    override var createdAt: LocalDate
) : UnmodifiablePost {
  override fun toString(): String {
    return "The post from the user ${userId} with the following description ${description}" +
        " and the following location ${location}" +
        " for following meal ${meal}"
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

  // Useful function for the ui
  fun getIngredientCalories(): List<Pair<String, Nutrient>> {
    return meal.ingredients.mapNotNull { ingredient ->
      val nutrient =
          ingredient.nutritionalInformation.nutrients.firstOrNull { it.nutrientType == "calories" }
      if (nutrient != null) Pair(ingredient.name, nutrient) else null
    }
  }

  // Really useful function for the ui
  fun getIngredientWeight(): List<Pair<String, Nutrient>> {
    return meal.ingredients.mapNotNull { ingredient ->
      Pair(ingredient.name, Nutrient("totalWeight", ingredient.amount, ingredient.unit))
    }
  }

  companion object {
    fun serialize(data: Post): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["userId"] = data.userId
        this["description"] = data.description
        this["location"] = data.location
        this["meal"] = data.meal.serialize()
        this["createdAt"] = data.createdAt
      }
    }

    fun deserialize(data: MutableMap<String, Any>): Post? {
      return try {
        val userId = data["userId"] as String

        val description = data["description"] as? String ?: ""
        val location = Location.deserialize(data["location"] as MutableMap<String, Any>)
        val meal = Meal.deserialize(data["meal"] as MutableMap<String, Any>)
        val createdAt = deserializeLocalDate(data, "createdAt") ?: LocalDate.now()

        val newPost = Post(userId, description, location, meal, createdAt)

        newPost
      } catch (e: Exception) {
        Log.e("Post", "Failed to deserialize Post object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Post object", e)
      }
    }

    private fun deserializeLocalDate(data: Map<String, Any>, key: String): LocalDate? {
      return try {
        val dateString = data[key] as? String
        dateString?.let { LocalDate.parse(it) }
      } catch (e: Exception) {
        throw IllegalArgumentException("Failed to deserialize LocalDate object", e)
      }
    }

    fun default(): Post {
      return Post(
          "testId",
          "Description",
          Location(0.0, 0.0, 10.0, "EPFL"),
          Meal.default(),
          LocalDate.now())
    }
  }
}
