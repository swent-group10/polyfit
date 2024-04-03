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
    var mealTemp: Double = 20.0,
    val nutritionalInformation: NutritionalInformation,
    val ingredients: MutableList<Ingredient> = mutableListOf()
) {

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

    companion object {

        fun serialize(data: Meal): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                this["mealID"] = data.mealID
                this["occasion"] = data.occasion.name
                this["name"] = data.name
                this["mealTemp"] = data.mealTemp
                this["nutritionalInformation"] =
                    NutritionalInformation.serialize(data.nutritionalInformation)
            }
        }

        fun deserialize(data: Map<String, Any>): Meal? {
            return try {
                val mealID = data["mealID"] as Int
                val occasion =
                    data["occasion"]?.let { MealOccasion.valueOf(it as String) } as MealOccasion
                val mealTemp = data["mealTemp"] as Double
                val name = data["name"] as String
                val nutritionalInformationData =
                    NutritionalInformation.deserialize(
                        data["nutritionalInformation"] as List<Map<String, Any>>
                    )
                val newMeal = Meal(occasion, name, mealID, mealTemp, nutritionalInformationData)

                newMeal
            } catch (e: Exception) {
                Log.e("Meal", "Failed to deserialize Meal object")
                throw Exception("Failed to deserialize Meal object")
            }
        }
    }
}
