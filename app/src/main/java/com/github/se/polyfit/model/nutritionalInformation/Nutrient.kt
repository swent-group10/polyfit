package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import com.github.se.polyfit.ui.utils.titleCase

/**
 * Represents a nutrient in a food item.
 *
 * @param nutrientType The type of nutrient.
 * @param amount The amount of the nutrient.
 * @param unit The unit of measurement for the nutrient.
 */
data class Nutrient(val nutrientType: String, val amount: Double, val unit: MeasurementUnit) {
  override fun toString(): String {
    return "$nutrientType :  $amount $unit"
  }

  fun toStringNoType(): String {
    return "$amount ${unit.toString().lowercase()}"
  }

  fun convertToUnit(newUnit: MeasurementUnit): Nutrient {
    val convertedAmount = MeasurementUnit.unitConversion(this.unit, newUnit, this.amount)
    val wasItConverted = (convertedAmount != this.amount)
    val newUnit = if (wasItConverted) newUnit else this.unit

    return Nutrient(this.nutrientType, convertedAmount, newUnit)
  }

  fun deepCopy(): Nutrient {
    return Nutrient(nutrientType, amount, unit)
  }

  fun getFormattedName(): String {
    return titleCase(nutrientType)
  }

  fun getFormattedAmount(): String {
    return "${amount.toInt()} ${unit.toString().lowercase()}"
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

    /**
     * Deserializes a map to a Nutrient object.
     *
     * @param data The map to deserialize.
     * @return The Nutrient object.
     * @throws IllegalArgumentException if cannot deserialize the map.
     */
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

    fun plus(nutrient1: Nutrient, nutrient2: Nutrient): Nutrient {
      return if (nutrient1.nutrientType != nutrient2.nutrientType) {
        throw IllegalArgumentException("Nutrient types do not match")
      } else if (nutrient1.unit == nutrient2.unit) {
        Nutrient(nutrient1.nutrientType, nutrient1.amount + nutrient2.amount, nutrient1.unit)
      } else {
        val convertedAmount =
            MeasurementUnit.unitConversion(nutrient2.unit, nutrient1.unit, nutrient2.amount)
        Nutrient(nutrient1.nutrientType, nutrient1.amount + convertedAmount, nutrient1.unit)
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
