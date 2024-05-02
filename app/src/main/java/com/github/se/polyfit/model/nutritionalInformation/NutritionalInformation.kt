package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log

class NutritionalInformation {
  val nutrients: MutableList<Nutrient> = mutableListOf()

  constructor(nutrientsList: MutableList<Nutrient>) {
    nutrients.addAll(nutrientsList.map { it.deepCopy() })
  }

  fun calculateTotalNutrient(nutrientType: String): Double {
    return nutrients
        .filter { it.nutrientType.equals(nutrientType, ignoreCase = true) }
        .map { it.amount }
        .sum()
  }

  fun deepCopy(): NutritionalInformation {
    val newNutritionalInformation = NutritionalInformation(mutableListOf())
    nutrients.forEach { newNutritionalInformation.update(it.copy()) }
    return newNutritionalInformation
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is NutritionalInformation) return false

    return this.nutrients == other.nutrients
  }

  override fun hashCode(): Int {
    var newHash = 0
    for (nutrient in nutrients) {
      newHash += 31 * nutrient.hashCode()
    }

    return newHash
  }

  /**
   * Update the nutritional information with new values. If the nutrient type already exists, the
   * amount is updated. If the nutrient type does not exist, a new nutrient is added. We make sure
   * all nutrients have positive amounts.
   *
   * @param newValues the new values to update the nutritional information with
   */
  fun update(newValues: Nutrient) {
    nutrients
        .map { it.nutrientType }
        .contains(newValues.nutrientType)
        .let {
          if (it) {
            val index = nutrients.indexOfFirst { it.nutrientType == newValues.nutrientType }

            nutrients[index] = (nutrients[index] + newValues)!!
          } else if (newValues.amount >= 0.0) {
            nutrients.add(newValues.deepCopy())
          }
        }

    val anyRemoved = nutrients.removeIf { it.amount < 0.0 }

    if (anyRemoved) Log.d("NutritionalInformation", "Removed negative nutrient(s)")
  }

  fun update(newValues: NutritionalInformation) {
    newValues.nutrients.forEach { update(it) }
  }

  fun serialize(): List<Map<String, Any>> {
    return serialize(this)
  }

  operator fun plus(other: NutritionalInformation): NutritionalInformation {
    val newNutritionalInformation = NutritionalInformation(mutableListOf())

    nutrients.forEach { newNutritionalInformation.update(it) }
    other.nutrients.forEach { newNutritionalInformation.update(it) }

    return newNutritionalInformation
  }

  operator fun minus(other: NutritionalInformation): NutritionalInformation {
    val newNutritionalInformation = NutritionalInformation(mutableListOf())

    nutrients.forEach { newNutritionalInformation.update(it) }
    other.nutrients.forEach { newNutritionalInformation.update(it * -1.0) }

    return newNutritionalInformation
  }

  fun getNutrient(nutrientType: String): Nutrient? {
    return nutrients.find { it.nutrientType.lowercase() == nutrientType.lowercase() }
  }

  companion object {
    fun serialize(nutritionalInformation: NutritionalInformation): List<Map<String, Any>> {
      return nutritionalInformation.nutrients.map { Nutrient.serialize(it) }
    }

    fun deserialize(data: List<Map<String, Any>>): NutritionalInformation {
      val nutritionalInformation = NutritionalInformation(mutableListOf())
      data.forEach {
        try {

          nutritionalInformation.update(Nutrient.deserialize(it))
        } catch (e: Exception) {
          Log.e(
              "NutritionalInformation",
              "Failed to deserialize NutritionalInformation object: ${e.message}",
              e)
          throw IllegalArgumentException(
              "Failed to deserialize NutritionalInformation object " + e.message)
        }
      }
      return nutritionalInformation
    }
  }
}
