package com.github.se.polyfit.model.post

/**
 * Location data class allows to store only the necessary information about the location without all
 * the other required information like in the android.location.Location class
 */

data class Location(
    val longitude: Double,
    val latitude: Double,
    val altitude: Double,
    val name: String
) {
    companion object {
        fun default(): Location {
            return Location(0.0, 0.0, 0.0, "EPFL")
        }

        fun deserialize(data: Map<String, Any>): Location {
            return Location(
                data["longitude"] as Double,
                data["latitude"] as Double,
                data["altitude"] as? Double ?: 0.0,
                data["name"] as? String ?: ""
            )
        }
    }
}
