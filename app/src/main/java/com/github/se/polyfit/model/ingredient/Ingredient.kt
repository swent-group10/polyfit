package com.github.se.polyfit.model.ingredient

import android.net.Uri
import android.util.Log
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.util.Locale

/**
 * Represents an ingredient in a meal. Automatically updates the parent meal when any property
 * changes.
 *
 * @property name is unique for each meal
 * @property id is the unique id given in API response
 * @property nutritionalInformation is the nutritional information of the ingredient
 * @property imageUri is the uri of the image of the ingredient (Spoonacular API)
 */
data class Ingredient(
    val name: String, // name is now a val
    val id: Long,
    val amount: Double,
    val unit: MeasurementUnit,
    val nutritionalInformation: NutritionalInformation = NutritionalInformation(),
    val imageUri: Uri = Uri.EMPTY,
) {

  fun deepCopy(): Ingredient {
    return Ingredient(name, id, amount, unit, nutritionalInformation.deepCopy())
  }

  private fun isInteger(d: Double): Boolean {
    return (d == d.toInt().toDouble())
  }

  fun amountFormatted(): String {
    Log.d("Ingredient", "displayInformation: $name, amount: $amount, unit: $unit")
    val amount = if (isInteger(amount)) amount.toInt() else amount
    return when (unit) {
      MeasurementUnit.OTHER -> "$amount $name"
      else -> "$name $amount ${unit.toString().lowercase(Locale.ROOT)}"
    }
  }

  companion object {
    fun serialize(ingredient: Ingredient): Map<String, Any> {
      val map = mutableMapOf<String, Any>()
      map["name"] = ingredient.name
      map["id"] = ingredient.id
      map["amount"] = ingredient.amount
      map["unit"] = ingredient.unit.toString()
      map["nutritionalInformation"] =
          NutritionalInformation.serialize(ingredient.nutritionalInformation)
      return map
    }

    fun increaseAmount(ingredient: Ingredient, amount: Double): Ingredient {
      return Ingredient(
          ingredient.name,
          ingredient.id,
          ingredient.amount + amount,
          ingredient.unit,
          ingredient.nutritionalInformation)
    }

    /**
     * Deserializes a map into an Ingredient object.
     *
     * @param data The map to deserialize.
     * @return The deserialized Ingredient object.
     * @throws IllegalArgumentException If the map cannot be deserialized into an Ingredient object.
     */
    fun deserialize(data: Map<String, Any>): Ingredient {
      return try {
        val name = data["name"] as String
        val id = data["id"] as Long
        val amount = data["amount"] as Double
        val unit = MeasurementUnit.fromString(data["unit"] as String)
        val nutValue = data["nutritionalInformation"] as List<Map<String, Any>>

        val nutritionalInformation = NutritionalInformation.deserialize(nutValue)
        Ingredient(name, id, amount, unit, nutritionalInformation)
      } catch (e: Exception) {
        Log.e("Ingredient", "Failed to deserialize Ingredient object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Ingredient object", e)
      }
    }

    fun default(): Ingredient {
      return Ingredient("tomato", 2, 123.4, MeasurementUnit.G)
    }
  }
}
