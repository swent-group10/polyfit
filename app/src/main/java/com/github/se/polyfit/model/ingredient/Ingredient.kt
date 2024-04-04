package com.github.se.polyfit.model.ingredient

import android.util.Log
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

/**
 * Represents an ingredient in a meal. Automatically updates the parent meal when any property
 * changes.
 *
 * @property name is unique for each meal
 * @property id is the unique id given in API response
 * @property nutritionalInformation
 */
data class Ingredient(
    val name: String, // name is now a val
    val id: Int,
    val nutritionalInformation: NutritionalInformation,
) {

  companion object {
    fun serialize(ingredient: Ingredient): Map<String, Any> {
      val map = mutableMapOf<String, Any>()
      map["name"] = ingredient.name
      map["id"] = ingredient.id
      map["nutritionalInformation"] =
          NutritionalInformation.serialize(ingredient.nutritionalInformation)
      return map
    }

    fun deserialize(data: Map<String, Any>): Ingredient {
      return try {
        val name = data["name"] as String
        val id = (data["id"] as Long).toInt()
        val nutValue = data["nutritionalInformation"] as List<Map<String, Any>>

        val nutritionalInformation = NutritionalInformation.deserialize(nutValue)

        Ingredient(name, id, nutritionalInformation)
      } catch (e: Exception) {
        Log.e("Ingredient", "Failed to deserialize Ingredient object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Ingredient object", e)
      }
    }
  }
}
