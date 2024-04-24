package com.github.se.polyfit.model.post

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

/**
 * Location data class allows to store only the necessary information about the location without all
 * the other required information like in the android.location.Location class
 */
data class Location(
    val longitude: Double,
    val latitude: Double,
    val altitude: Double,
    val name: String
) {
    companion object {
        fun default(): Location {
            return Location(0.0, 0.0, 0.0, "EPFL")
        }
    }
}
