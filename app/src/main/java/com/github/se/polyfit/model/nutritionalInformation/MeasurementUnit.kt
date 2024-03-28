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
        fun fromString(unit: String): MeasurementUnit {
            return when (unit.uppercase(Locale.getDefault())) {
                "G" -> G
                "MG" -> MG
                "UG" -> UG
                "IU" -> IU
                "CAL" -> CAL
                "UNIT" -> UNIT
                else -> NONE
            }
        }
    }
}