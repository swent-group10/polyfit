package com.github.se.polyfit.model.nutritionalInformation

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
    fun fromString(unit: String): MeasurementUnit? {
      return when (unit.uppercase(Locale.getDefault())) {
        "G" -> G
        "MG" -> MG
        "UG" -> UG
        "IU" -> IU
        "CAL" -> CAL
        "UNIT" -> UNIT
        "NONE" -> NONE
        else -> null
      }
    }

    // Function that converts units into each other
    fun unitConversion(from: MeasurementUnit, to: MeasurementUnit, value: Double): Double {
      return when (from) {
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
    }
  }
}
