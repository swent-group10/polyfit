package com.github.se.polyfit.model.ingredient

import com.github.se.polyfit.model.meal.Meal
import kotlin.properties.Delegates

/**
 * Represents an ingredient in a meal. Automatically updates the parent meal when any property
 * changes.
 *
 * @property name is unique for each meal
 */
class Ingredient(
    val name: String, // name is now a val
    servingSize: Double,
    calories: Double,
    protein: Double,
    carbohydrates: Double,
    fat: Double
) {
  var servingSize: Double by
      Delegates.observable(servingSize) { _, _, _ -> parentMeal?.updateMeal() }
  var calories: Double by Delegates.observable(calories) { _, _, _ -> parentMeal?.updateMeal() }
  var protein: Double by Delegates.observable(protein) { _, _, _ -> parentMeal?.updateMeal() }
  var carbohydrates: Double by
      Delegates.observable(carbohydrates) { _, _, _ -> parentMeal?.updateMeal() }
  var fat: Double by Delegates.observable(fat) { _, _, _ -> parentMeal?.updateMeal() }

  var parentMeal: Meal? = null

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Ingredient

    if (name != other.name) return false
    if (servingSize != other.servingSize) return false
    if (calories != other.calories) return false
    if (protein != other.protein) return false
    if (carbohydrates != other.carbohydrates) return false
    if (fat != other.fat) return false

    return true
  }

  companion object {
    /** Converts an Ingredient to a Map. */
    fun serializeIngredient(data: Ingredient): Map<String, Any> {
      val map = mutableMapOf<String, Any>()

      map["name"] = data.name
      map["servingSize"] = data.servingSize
      map["calories"] = data.calories
      map["protein"] = data.protein
      map["carbohydrates"] = data.carbohydrates
      map["fat"] = data.fat

      return map
    }

    /** Converts a Map to an Ingredient. Throws an exception if a required field is missing. */
    fun deserializeIngredient(data: Map<String, Any>): Ingredient {
      val name = requireNotNull(data["name"]) { "Missing 'name'" } as String
      val servingSize = requireNotNull(data["servingSize"]) { "Missing 'servingSize'" } as Double
      val calories = requireNotNull(data["calories"]) { "Missing 'calories'" } as Double
      val protein = requireNotNull(data["protein"]) { "Missing 'protein'" } as Double
      val carbohydrates =
          requireNotNull(data["carbohydrates"]) { "Missing 'carbohydrates'" } as Double
      val fat = requireNotNull(data["fat"]) { "Missing 'fat'" } as Double

      return Ingredient(name, servingSize, calories, protein, carbohydrates, fat)
    }
  }
}
