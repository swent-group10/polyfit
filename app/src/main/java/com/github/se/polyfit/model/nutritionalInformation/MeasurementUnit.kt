package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import kotlin.math.round

enum class MeasurementUnit {
  G,
  MG,
  UG,
  IU,
  CAL,
  KCAL,
  UNIT,
  ML,
  CUPS,
  TABLESPOONS,
  TEASPOONS,
  PINCHES,
  LB,
  LEAVES,
  SERVINGS,
  NONE;

  override fun toString(): String = this.name

  companion object {
    private val conversionTable: Map<Pair<MeasurementUnit, MeasurementUnit>, Double> =
        mapOf(
                Pair(G, LB) to 0.00220462,
                Pair(G, MG) to 1000.0,
                Pair(G, UG) to 1_000_000.0,
                Pair(MG, G) to 0.001,
                Pair(MG, UG) to 1000.0,
                Pair(UG, G) to 0.000_001,
                Pair(UG, MG) to 0.001,
                Pair(ML, CUPS) to 0.00416667,
                Pair(ML, TABLESPOONS) to 0.0666667,
                Pair(ML, TEASPOONS) to 0.2,
                Pair(CUPS, ML) to 240.0,
                Pair(TABLESPOONS, ML) to 15.0,
                Pair(TEASPOONS, ML) to 5.0,
                Pair(CAL, KCAL) to 0.001,
                Pair(KCAL, CAL) to 1000.0,
                Pair(LB, G) to 453.592,
                Pair(LB, MG) to 453_592.37,
                Pair(LB, UG) to 453_592_370.0)
            .withDefault { 1.0 }

    fun fromString(unit: String): MeasurementUnit =
        when (unit.lowercase()) {
          "g" -> G
          "mg" -> MG
          "ug",
          "µg" -> UG
          "iu" -> IU
          "cal",
          "calories" -> CAL
          "kcal" -> KCAL
          "unit" -> UNIT
          "ml" -> ML
          "cups",
          "cup" -> CUPS
          "tablespoons",
          "tablespoon" -> TABLESPOONS
          "teaspoons",
          "teaspoon" -> TEASPOONS
          "pinches",
          "pinch" -> PINCHES
          "lb" -> LB
          "leaves" -> LEAVES
          "servings" -> SERVINGS
          "" -> NONE
          else -> throw IllegalArgumentException("Invalid unit: $unit ${unit.uppercase()}")
        }

    fun unitConversion(from: MeasurementUnit, to: MeasurementUnit, value: Double): Double {
      if (from == UNIT || to == UNIT || from == NONE || to == NONE) {
        Log.e("MeasurementUnit", "Unsupported conversion from $from to $to")
        throw IllegalArgumentException("Unsupported conversion from $from to $to")
      } else if (value < 0) {
        Log.e("MeasurementUnit", "Value cannot be negative")
        throw IllegalArgumentException("Value cannot be negative")
      }
      val result = value * (conversionTable[Pair(from, to)] ?: 1.0)
      return round(result * 100) / 100
    }
  }
}
