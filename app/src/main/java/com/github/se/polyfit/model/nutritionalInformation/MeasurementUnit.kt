package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import java.util.Locale

enum class MeasurementUnit {
  G,
  MG,
  UG,
  IU,
  CAL,
  UNIT,
  NONE;

  companion object {
    fun fromString(unit: String): MeasurementUnit {
      return when (unit.uppercase(Locale.getDefault())) {
        "G" -> G
        "MG" -> MG
        "UG" -> UG
        "IU" -> IU
        "CAL" -> CAL
        "UNIT" -> UNIT
        "NONE" -> NONE
        else -> throw IllegalArgumentException("Invalid unit: $unit")
      }
    }

    /**
     * Convert a value from one unit to another.
     *
     * @param from the unit to convert from
     * @param to the unit to convert to
     * @param value the value to convert
     * @return the converted value with a rounded precision of two decimal places
     */
    fun unitConversion(from: MeasurementUnit, to: MeasurementUnit, value: Double): Double {
      if (from == UNIT || to == UNIT || from == NONE || to == NONE) {
        throw IllegalArgumentException("Unsupported conversion from $from to $to")
        Log.e("MeasurementUnit", "Unsupported conversion from $from to $to")
      } else if (value < 0) {
        throw IllegalArgumentException("Value cannot be negative")
        Log.e("MeasurementUnit", "Value cannot be negative")
      }

      val result =
          when (from) {
            G ->
                when (to) {
                  G -> value
                  MG -> value * 1000
                  UG -> value * 1000000
                  else -> value
                }
            MG ->
                when (to) {
                  G -> value / 1000
                  MG -> value
                  UG -> value * 1000
                  else -> value
                }
            UG ->
                when (to) {
                  G -> value / 1000000
                  MG -> value / 1000
                  UG -> value
                  else -> value
                }
            else -> value
          }

      return Math.round(result * 100.0) / 100.0
    }
  }
}
