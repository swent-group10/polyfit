package com.github.se.polyfit.model.meal

import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

// modeled after the log meal api
data class Meal(
    val occasion: MealOccasion,
    val name: String,
    val nutritionalInformation: NutritionalInformation,
    private val ingredients: MutableList<Ingredient> = mutableListOf()
) {
    private var uid: String = ""

    // Unique Id chosen by the function MealFirebaseConnection.addMeal
    fun updateUid(newUid: String) {
        if (uid != "") throw Exception("uid already set")
        uid = newUid
    }

    fun getUid(): String {
        return uid
    }

    fun addIngredient(ingredient: Ingredient) {
        ingredients.add(ingredient)
        updateMeal()
    }

    fun updateMeal() {
//        calories = ingredients.sumOf { it.calories }
//        protein = ingredients.sumOf { it.protein }
//        carbohydrates = ingredients.sumOf { it.carbohydrates }
//        fat = ingredients.sumOf { it.fat }
    }

    companion object {

        /**
         * Serializes a Meal object to a pair of maps that can be stored in Firestore. They need to be
         * meals and ingredients need to be deserialized separately. In order to facilitate storing them
         * on the firestore database.
         *
         * @param data The Meal object to serialize.
         * @return A pair of maps. The first map contains the Meal properties, and the second a list of
         *   map that contains the Ingredient properties.
         */
//        fun serializeMeal(data: Meal): Pair<Map<String, Any>> {
//            val map = mutableMapOf<String, Any>()
//
//            // Convert each property manually
//            map["uid"] = data.uid
//            map["occasion"] = data.occasion.name
//            map["name"] = data.name
//            map["calories"] = data.calories
//            map["protein"] = data.protein
//            map["carbohydrates"] = data.carbohydrates
//            map["fat"] = data.fat
//
//            val ingredientsSerialList = mutableListOf<Map<String, Any>>()
//            data.ingredients.forEach { ingredient ->
//                ingredientsSerialList.add(Ingredient.serializeIngredient(ingredient))
//            }
//
//            return Pair(map, ingredientsSerialList)
//        }
//
//        fun deserializeMeal(data: Map<String, Any>, ingredientsMap: List<Map<String, Any>>): Meal {
//            // Convert each property manually
//            val uid = data["uid"] as String
//            val occasion = MealOccasion.valueOf(data["occasion"] as String)
//            val name = data["name"] as String
//            val calories = data["calories"] as Double
//            val protein = data["protein"] as Double
//            val carbohydrates = data["carbohydrates"] as Double
//            val fat = data["fat"] as Double
//
//            val ingredients =
//                ingredientsMap.map { Ingredient.deserializeIngredient(it) }.toMutableList()
//
//            return Meal(occasion, name, calories, protein, carbohydrates, fat)
//                .apply { updateUid(uid) }
//                .apply { ingredients.forEach { addIngredient(it) } }
//        }
//    }
    }
}
