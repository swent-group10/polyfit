package com.github.se.polyfit.data.post

import com.github.se.polyfit.model.post.Location
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationTest {

  @Test
  fun `deserialize returns correct Location when all fields are present and correctly formatted`() {
    val data =
        mapOf("longitude" to 1.0, "latitude" to 2.0, "altitude" to 3.0, "name" to "Test Location")

    val location = Location.deserialize(data)

    assertEquals(1.0, location.longitude, 0.0)
    assertEquals(2.0, location.latitude, 0.0)
    assertEquals(3.0, location.altitude, 0.0)
    assertEquals("Test Location", location.name)
  }

  @Test
  fun `deserialize returns correct Location when altitude field is missing`() {
    val data =
        mapOf(
            "longitude" to 1.0,
            "latitude" to 2.0,
            // "altitude" field is missing
            "name" to "Test Location")

    val location = Location.deserialize(data)

    assertEquals(1.0, location.longitude, 0.0)
    assertEquals(2.0, location.latitude, 0.0)
    assertEquals(0.0, location.altitude, 0.0) // Altitude should default to 0.0
    assertEquals("Test Location", location.name)
  }

  @Test
  fun `deserialize returns correct Location when name field is missing`() {
    val data =
        mapOf(
            "longitude" to 1.0, "latitude" to 2.0, "altitude" to 3.0
            // "name" field is missing
            )

    val location = Location.deserialize(data)

    assertEquals(1.0, location.longitude, 0.0)
    assertEquals(2.0, location.latitude, 0.0)
    assertEquals(3.0, location.altitude, 0.0)
    assertEquals("", location.name) // Name should default to an empty string
  }
}
