package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log

data class NutritionalInformation(
    val totalWeight: Nutrient = Nutrient(),
    val calories: Nutrient = Nutrient(),
    val fat: Nutrient = Nutrient(),
    val saturatedFat: Nutrient = Nutrient(),
    val carbohydrates: Nutrient = Nutrient(),
    val netCarbohydrates: Nutrient = Nutrient(),
    val sugar: Nutrient = Nutrient(),
    val cholesterol: Nutrient = Nutrient(),
    val sodium: Nutrient = Nutrient(),
    val protein: Nutrient = Nutrient(),
    val vitaminC: Nutrient = Nutrient(),
    val manganese: Nutrient = Nutrient(),
    val fiber: Nutrient = Nutrient(),
    val vitaminB6: Nutrient = Nutrient(),
    val copper: Nutrient = Nutrient(),
    val vitaminB1: Nutrient = Nutrient(),
    val folate: Nutrient = Nutrient(),
    val potassium: Nutrient = Nutrient(),
    val magnesium: Nutrient = Nutrient(),
    val vitaminB3: Nutrient = Nutrient(),
    val vitaminB5: Nutrient = Nutrient(),
    val vitaminB2: Nutrient = Nutrient(),
    val iron: Nutrient = Nutrient(),
    val calcium: Nutrient = Nutrient(),
    val vitaminA: Nutrient = Nutrient(),
    val zinc: Nutrient = Nutrient(),
    val phosphorus: Nutrient = Nutrient(),
    val vitaminK: Nutrient = Nutrient(),
    val selenium: Nutrient = Nutrient(),
    val vitaminE: Nutrient = Nutrient(),
) {

  fun serialize(): Map<String, Map<String, Any>> {
    return serialize(this)
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
