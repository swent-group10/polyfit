package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import java.util.Locale
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
    NONE;


    override fun toString(): String {
        return when (this) {
            G -> "G"
            MG -> "MG"
            UG -> "UG"
            IU -> "IU"
            KCAL -> "KCAL"
            CAL -> "CAL"
            UNIT -> "UNIT"
            ML -> "ML"
            CUPS -> "CUPS"
            TABLESPOONS -> "TABLESPOONS"
            TEASPOONS -> "TEASPOONS"
            PINCHES -> "PINCHES"
            NONE -> ""
        }
    }

    companion object {
        fun fromString(unit: String): MeasurementUnit {
            Log.d(
                "MeasurementUnit",
                "Converting unit string to MeasurementUnit: ${unit.uppercase(Locale.getDefault())}"
            )
            return when (unit.lowercase()) {
                "g" -> G
                "mg" -> MG
                "ug", "µg" -> UG
                "iu" -> IU
                "cal" -> CAL
                "unit" -> UNIT
                "ml" -> ML
                "kcal" -> KCAL
                "cups", "cup" -> CUPS
                "tablespoons", "tablespoon" -> TABLESPOONS
                "teaspoons", "teaspoon" -> TEASPOONS
                "pinches", "pinch" -> PINCHES
                "calories" -> CAL
                "" -> NONE
                else -> throw IllegalArgumentException("Invalid unit: $unit ${unit.uppercase(Locale.getDefault())}")
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
                Log.e("MeasurementUnit", "Unsupported conversion from $from to $to")
                throw IllegalArgumentException("Unsupported conversion from $from to $to")
            } else if (value < 0) {
                Log.e("MeasurementUnit", "Value cannot be negative")
                throw IllegalArgumentException("Value cannot be negative")
            }

            val result = when (from) {
                G -> when (to) {
                    G -> value
                    MG -> value * 1000
                    UG -> value * 1000000
                    else -> value
                }

                MG -> when (to) {
                    G -> value / 1000
                    MG -> value
                    UG -> value * 1000
                    else -> value
                }

                UG -> when (to) {
                    G -> value / 1000000
                    MG -> value / 1000
                    UG -> value
                    else -> value
                }

                ML -> when (to) {
                    ML -> value
                    CUPS -> value / 240
                    TABLESPOONS -> value / 15
                    TEASPOONS -> value / 5
                    else -> value
                }

                CUPS -> when (to) {
                    ML -> value * 240
                    CUPS -> value
                    TABLESPOONS -> value * 16
                    TEASPOONS -> value * 48
                    else -> value
                }

                TABLESPOONS -> when (to) {
                    ML -> value * 15
                    CUPS -> value / 16
                    TABLESPOONS -> value
                    TEASPOONS -> value * 3
                    else -> value
                }

                TEASPOONS -> when (to) {
                    ML -> value * 5
                    CUPS -> value / 48
                    TABLESPOONS -> value / 3
                    TEASPOONS -> value
                    else -> value
                }

                PINCHES -> when (to) {
                    PINCHES -> value
                    else -> value
                }

                CAL -> when (to) {
                    CAL -> value
                    KCAL -> value / 1000
                    else -> value
                }

                KCAL -> when (to) {
                    CAL -> value * 1000
                    KCAL -> value
                    else -> value
                }

                else -> value
            }

            return round(result * 100) / 100
        }
    }
}
