package com.github.se.polyfit.model.meal

// modeled after the log meal api
data class Meal(
    val uid: String,
    val occasion: MealOccasion,
    val name: String,
    val calories: Double,
    val protein: Double,
    val carbohydrates: Double,
    val fat: Double
) {
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
                fat
            )
        }
    }
}