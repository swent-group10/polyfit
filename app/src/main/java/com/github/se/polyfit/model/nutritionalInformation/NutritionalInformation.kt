package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import kotlin.reflect.KMutableProperty1

data class NutritionalInformation(
    var totalWeight: Nutrient = Nutrient(),
    var calories: Nutrient = Nutrient(),
    var fat: Nutrient = Nutrient(),
    var saturatedFat: Nutrient = Nutrient(),
    var carbohydrates: Nutrient = Nutrient(),
    var netCarbohydrates: Nutrient = Nutrient(),
    var sugar: Nutrient = Nutrient(),
    var cholesterol: Nutrient = Nutrient(),
    var sodium: Nutrient = Nutrient(),
    var protein: Nutrient = Nutrient(),
    var vitaminC: Nutrient = Nutrient(),
    var manganese: Nutrient = Nutrient(),
    var fiber: Nutrient = Nutrient(),
    var vitaminB6: Nutrient = Nutrient(),
    var copper: Nutrient = Nutrient(),
    var vitaminB1: Nutrient = Nutrient(),
    var folate: Nutrient = Nutrient(),
    var potassium: Nutrient = Nutrient(),
    var magnesium: Nutrient = Nutrient(),
    var vitaminB3: Nutrient = Nutrient(),
    var vitaminB5: Nutrient = Nutrient(),
    var vitaminB2: Nutrient = Nutrient(),
    var iron: Nutrient = Nutrient(),
    var calcium: Nutrient = Nutrient(),
    var vitaminA: Nutrient = Nutrient(),
    var zinc: Nutrient = Nutrient(),
    var phosphorus: Nutrient = Nutrient(),
    var vitaminK: Nutrient = Nutrient(),
    var selenium: Nutrient = Nutrient(),
    var vitaminE: Nutrient = Nutrient(),
) {
  fun deepCopy(): NutritionalInformation {
    val copy = NutritionalInformation()
    NutritionalInformation::class
        .members
        .filterIsInstance<KMutableProperty1<NutritionalInformation, Nutrient>>()
        .forEach { property ->
          val thisValue = property.get(this)
          val copyValue = Nutrient(thisValue.amount, thisValue.unit)
          property.set(copy, copyValue)
        }

    // dont know if this this the correct way to handle this
    if (this === copy) {
      Log.e("NutritionalInformation", "Deep copy failed")
      throw Exception("Deep copy failed")
    }
    return copy
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is NutritionalInformation) return false

    NutritionalInformation::class
        .members
        .filterIsInstance<KMutableProperty1<NutritionalInformation, Nutrient>>()
        .forEach { property ->
          val thisValue = property.get(this)
          val otherValue = property.get(other)
          if (thisValue != otherValue) return false
        }

    return true
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }

  // Function that updates the current nutritional information
  fun update(newValues: NutritionalInformation) {
    NutritionalInformation::class
        .members
        .filterIsInstance<KMutableProperty1<NutritionalInformation, Nutrient>>()
        .forEach { property ->
          val thisValue = property.get(this)
          val newValue = property.get(newValues)
          thisValue.amount = newValue.amount
          thisValue.unit = newValue.unit
        }
  }

  fun serialize(): Map<String, Map<String, Any>> {
    return serialize(this)
  }

  operator fun plus(other: NutritionalInformation): NutritionalInformation {
    val result = NutritionalInformation()

    NutritionalInformation::class
        .members
        .filterIsInstance<KMutableProperty1<NutritionalInformation, Nutrient>>()
        .forEach { property ->
          val thisValue = property.get(this)
          val otherValue = property.get(other)
          val resultValue = Nutrient(thisValue.amount + otherValue.amount, thisValue.unit)

          property.set(result, resultValue)
        }

    return result
  }

  companion object {
    fun serialize(nutritionalInformation: NutritionalInformation): Map<String, Map<String, Any>> {
      return NutritionalInformation::class
          .members
          .filterIsInstance<kotlin.reflect.KProperty1<NutritionalInformation, *>>()
          .associate { property ->
            property.name to Nutrient.serialize((property.get(nutritionalInformation) as Nutrient))
          }
    }

    fun deserialize(data: Map<String, Map<String, Any>>): NutritionalInformation? {
      val constructor = NutritionalInformation::class.constructors.first()
      val parameters =
          constructor.parameters.associateWith { parameter ->
            data[parameter.name]?.let { nutrientData ->
              try {
                Nutrient.deserialize(nutrientData) as Nutrient
              } catch (e: Exception) {
                Log.e("NutritionalInformation", "Failed to deserialize Nutrient", e)
                return null
              }
            }
                ?: run {
                  Log.e(
                      "NutritionalInformation",
                      "Failed to deserialize Nutrient: ${parameter.name} not found in data")
                  return null
                }
          }
      return constructor.callBy(parameters)
    }
  }
}
