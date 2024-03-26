package com.github.se.polyfit.model.meal

import com.github.se.polyfit.model.ingredient.Ingredient

// modeled after the log meal api
data class Meal(
    val uid: String,
    private var occasion: MealOccasion,
    private var name: String,
    private var calories: Double,
    private var protein: Double,
    private var carbohydrates: Double,
    private var fat: Double,
    private var ingredients: List<Ingredient> = emptyList()
) {
    init {
        ingredients.forEach { it.parentMeal = this }
        updateMeal()
    }

    fun getCalories(): Double {
        return calories
    }

    fun getProtein(): Double {
        return protein
    }

    fun getCarbohydrates(): Double {
        return carbohydrates
    }

    fun getFat(): Double {
        return fat
    }

    fun getOccasion(): MealOccasion {
        return occasion
    }

    fun getName(): String {
        return name
    }

    fun addIngredient(ingredient: Ingredient) {
        ingredients = ingredients + ingredient
        ingredient.parentMeal = this
        updateMeal()
    }

    fun updateMeal() {
        calories = ingredients.sumOf { it.calories }
        protein = ingredients.sumOf { it.protein }
        carbohydrates = ingredients.sumOf { it.carbohydrates }
        fat = ingredients.sumOf { it.fat }

    }

    fun getAllIngredients(): List<Ingredient> {
        return ingredients
    }

    companion object {
        fun serializeMeal(data: Meal): Map<String, Any> {
            val map = mutableMapOf<String, Any>()

            // Convert each property manually
            map["uid"] = data.uid
            map["occasion"] = data.occasion.name
            map["name"] = data.name
            map["calories"] = data.calories
            map["protein"] = data.protein
            map["carbohydrates"] = data.carbohydrates
            map["fat"] = data.fat

            return map
        }

        fun deserializeMeal(data: Map<String, Any>): Meal {
            // Convert each property manually
            val uid = data["uid"] as String
            val occasion = MealOccasion.valueOf(data["occasion"] as String)
            val name = data["name"] as String
            val calories = data["calories"] as Double
            val protein = data["protein"] as Double
            val carbohydrates = data["carbohydrates"] as Double
            val fat = data["fat"] as Double


            return Meal(
                uid,
                occasion,
                name,
                calories,
                protein,
                carbohydrates,
                fat,
                emptyList()
            )
        }
    }
}