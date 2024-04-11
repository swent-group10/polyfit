package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import com.github.se.polyfit.ui.utils.titleCase

data class Nutrient(val nutrientType: String, val amount: Double, val unit: MeasurementUnit) {

  // setters

  override fun toString(): String {
    return "$nutrientType :  $amount $unit"
  }

  fun deepCopy(): Nutrient {
    return Nutrient(nutrientType, amount, unit)
  }

  fun getFormattedName(): String {
    return if (!isVitamin()) {
      val regex = "(?=[A-Z])".toRegex()
      titleCase(nutrientType.split(regex).joinToString(" "))
    } else {
      val vitaminType = nutrientType.substringAfter("vitamin", "")
      titleCase("vitamin $vitaminType")
    }
  }

  fun getFormattedAmount(): String {
    return "${amount.toInt()} ${unit.toString().lowercase()}"
  }

  private fun isVitamin(): Boolean {
    return nutrientType.startsWith("vitamin", ignoreCase = true)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Nutrient) return false

    return this.amount == other.amount &&
        this.unit == other.unit &&
        this.nutrientType == other.nutrientType
  }

  override fun hashCode(): Int {
    var result = 31 * amount.hashCode()
    result += 31 * unit.hashCode()
    result += 31 * nutrientType.hashCode()
    return result
  }

  operator fun times(factor: Double): Nutrient {
    return Nutrient(nutrientType, amount * factor, unit)
  }

  companion object {
    fun serialize(nutrient: Nutrient): Map<String, Any> {
      val map = mutableMapOf<String, Any>()
      map["nutrientType"] = nutrient.nutrientType
      map["amount"] = nutrient.amount
      map["unit"] = nutrient.unit.toString()

      return map
    }

    fun deserialize(data: Map<String, Any>): Nutrient {
      return try {
        val nutrientType = data["nutrientType"] as String
        val amount = data["amount"] as Double
        val unit = data["unit"] as String
        val measurementUnit = MeasurementUnit.fromString(unit)

        Nutrient(nutrientType, amount, measurementUnit)
      } catch (e: Exception) {
        Log.e("Nutrient", "Failed to deserialize Nutrient object : ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Nutrient object ", e)
      }
    }
  }

  /**
   * Overloaded operator function to add two Nutrient objects. If the units are the same, the
   * amounts are added directly. If the units are different, this function returns null.
   *
   * @param other the Nutrient object to add
   * @return the sum of the two Nutrient objects
   */
  operator fun plus(other: Nutrient): Nutrient? {
    return if (this.nutrientType != other.nutrientType) {
      return null
    } else if (this.unit == other.unit) {
      Nutrient(this.nutrientType, this.amount + other.amount, this.unit)
    } else {
      val convertedAmount = MeasurementUnit.unitConversion(other.unit, this.unit, other.amount)
      Nutrient(this.nutrientType, this.amount + convertedAmount, this.unit)
    }
  }
}
