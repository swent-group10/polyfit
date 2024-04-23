package com.github.se.polyfit.model.post

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
  }
}
