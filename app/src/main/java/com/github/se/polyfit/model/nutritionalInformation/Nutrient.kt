package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log

data class Nutrient(var amount: Double = 0.0, var unit: MeasurementUnit = MeasurementUnit.G) {

  // setters

  override fun toString(): String {
    return "$amount ${unit.name}"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Nutrient) return false

    return this.amount == other.amount && this.unit == other.unit
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }

  companion object {
    fun serialize(nutrient: Nutrient): Map<String, Any> {
      val map = mutableMapOf<String, Any>()

      map["amount"] = nutrient.amount
      map["unit"] = nutrient.unit.toString()

      return map
    }

    fun deserialize(data: Map<String, Any>): Nutrient? {
      return try {
        val amount = data["amount"] as Double
        val unit = data["unit"] as String
        val measurementUnit = MeasurementUnit.fromString(unit) as MeasurementUnit

        Nutrient(amount, measurementUnit)
      } catch (e: Exception) {
        Log.e("Nutrient", "Failed to deserialize Nutrient object")
        null
      }
    }
  }

  // override the add function
  operator fun plus(other: Nutrient): Nutrient {
    return if (this.unit == other.unit) {
      Nutrient(this.amount + other.amount, this.unit)
    } else {
      val convertedAmount = MeasurementUnit.unitConversion(other.unit, this.unit, other.amount)
      Nutrient(this.amount + convertedAmount, this.unit)
    }
  }
}
