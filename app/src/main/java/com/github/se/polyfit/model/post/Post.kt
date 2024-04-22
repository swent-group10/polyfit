package com.github.se.polyfit.model.post

import android.util.Log
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import java.time.LocalDate

data class Post(
    val userId: String,
    val description: String,
    //    val images: List<Bitmap>,
    val location: Location,
    val meal: Meal,
    val createdAt: LocalDate
) {
    override fun toString(): String {
        return "The post from the user ${userId} with the following description ${description}" +
                "containts  from the following location ${location}" +
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

    fun getIngredientWeight(): List<Pair<String, Nutrient>> {
        return meal.ingredients.mapNotNull { ingredient ->
            Pair(ingredient.name, Nutrient("totalWeight", ingredient.amount, ingredient.unit))
        }
    }

    companion object {
        // ToDo test this with the live database to make sure that it works properly
        fun serialize(data: Post): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                this["userId"] = data.userId
                this["description"] = data.description
                //        this["images"] = data.images
                this["location"] = data.location
                this["meal"] = data.meal.serialize()

                this["createdAt"] = data.createdAt
            }
        }

        fun deserialize(data: Map<String, Any>): Post {
            return try {
                val userID = data["userId"] as String
                val description = data["description"] as String
                //        val images = data["images"] as List<Bitmap>
                val location = data["location"] as Location
                val meal = Meal.deserialize(data["meal"] as Map<String, Any>)

                val createdAt = data["createdAt"] as LocalDate

                return Post(userID, description, location, meal, createdAt)
            } catch (e: Exception) {
                Log.e("Post", "Failed to deserialize Post object: ${e.message}", e)
                throw IllegalArgumentException("Failed to deserialize Post object", e)
            }
        }

        fun default(): Post {
            return Post(
                "testId",
                "Description",
                Location(0.0, 0.0, 10.0, "EPFL"),
                Meal.default(),
                LocalDate.now()
            )
        }
    }
}

data class Location(
    val longitude: Double,
    val latitude: Double,
    val altitude: Double,
    val name: String
)