package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

// modeled after the log meal api
data class Meal(
    val occasion: MealOccasion,
    val name: String,
    val mealID: Int,
    // represent the ideal temperature at which should be eaten at,
    // usefull for later features
    val mealTemp: Double = 20.0,
    val nutritionalInformation: NutritionalInformation,
    val ingredients: MutableList<Ingredient> = mutableListOf()
) {
    init {
        require(mealID >= 0)

        updateMeal()
    }

    fun addIngredient(ingredient: Ingredient) {
        ingredients.add(ingredient)
        updateMeal()
    }

    private fun updateMeal() {

        var newNutritionalInformation = NutritionalInformation(mutableListOf())

        for (ingredient in ingredients) {
            newNutritionalInformation += ingredient.nutritionalInformation
        }
        nutritionalInformation.update(newNutritionalInformation)
    }

    fun serialize(): Map<String, Any> {
        return serialize(this)
    }

    companion object {

        fun serialize(data: Meal): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                this["mealID"] = data.mealID
                this["occasion"] = data.occasion.name
                this["name"] = data.name
                this["mealTemp"] = data.mealTemp
                this["nutritionalInformation"] =
                    NutritionalInformation.serialize(data.nutritionalInformation)
                this["ingredients"] = data.ingredients.map { Ingredient.serialize(it) }
            }
        }

        fun deserialize(data: Map<String, Any>): Meal? {
            return try {
                Log.d("Meal", "Deserializing meal data: $data")
                val mealID = (data["mealID"] as Long).toInt()
                Log.d("Meal", "Deserializing mealId: $mealID")
                val occasion =
                    data["occasion"]?.let { MealOccasion.valueOf(it as String) } as MealOccasion
                Log.d(
                    "Meal",
                    "Deserializing mealOccasion: ${data["occasion"]} as $occasion"
                )
                val mealTemp = data["mealTemp"] as Double
                Log.d(
                    "Meal",
                    "Deserializing mealTemp: ${data["mealTemp"]} as $mealTemp"
                )
                val name = data["name"] as String
                Log.d(
                    "Meal",
                    "Deserializing mealName: ${data["name"]} as $name"
                )
                val nutritionalInformationData =
                    NutritionalInformation.deserialize(
                        data["nutritionalInformation"] as List<Map<String, Any>>
                    )
                Log.d(
                    "Meal",
                    "Deserializing mealNutritionalInformation: ${data["nutritionalInformation"]} as $nutritionalInformationData"
                )
                val newMeal = Meal(occasion, name, 11, mealTemp, nutritionalInformationData)

                val ingredientsData = data["ingredients"] as List<Map<String, Any>>
                for (ingredientData in ingredientsData) {
                    newMeal.addIngredient(Ingredient.deserialize(ingredientData))
                }
                newMeal
            } catch (e: Exception) {
                Log.e("Meal", "Failed to deserialize Meal object: ${e.message}", e)
                throw IllegalArgumentException("Failed to deserialize Meal object", e)
            }
        }
    }
}
