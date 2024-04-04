package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import kotlin.test.assertFailsWith
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NutrientTest {

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun nutrientSerializationAndDeserialization() {
    val nutrient = Nutrient("fats", 100.0, MeasurementUnit.G)
    val serializedData = Nutrient.serialize(nutrient)
    val expectedData =
        mapOf("nutrientType" to "fats", "amount" to 100.0, "unit" to MeasurementUnit.G.name)
    Assert.assertEquals(expectedData, serializedData)

    val deserializedData = Nutrient.deserialize(serializedData)
    Assert.assertEquals(nutrient, deserializedData)
  }

  @Test
  fun additionOfSameUnitNutrients() {
    val nutrient1 = Nutrient("fats", 100.0, MeasurementUnit.G)
    val nutrient2 = Nutrient("fats", 200.0, MeasurementUnit.G)
    val result = nutrient1 + nutrient2
    Assert.assertNotNull(result)
    Assert.assertEquals(300.0, result!!.amount, 0.001)
    Assert.assertEquals(MeasurementUnit.G, result.unit)
  }

  @Test
  fun additionOfDifferentUnitNutrients() {
    val nutrient1 = Nutrient("fats", 1.0, MeasurementUnit.G)
    val nutrient2 = Nutrient("fats", 1000.0, MeasurementUnit.MG)

    val result = nutrient1 + (nutrient2)
    Assert.assertNotNull(result)

    Assert.assertEquals(2.0, result!!.amount, 0.001)
    Assert.assertEquals(MeasurementUnit.G, result.unit)
  }

  @Test
  fun testNutrientSerializationAndDeserialization() {
    val nutrient = Nutrient("fats", 100.0, MeasurementUnit.G)
    val serializedData = Nutrient.serialize(nutrient)
    val expectedData =
        mapOf("nutrientType" to "fats", "amount" to 100.0, "unit" to MeasurementUnit.G.name)
    Assert.assertEquals(expectedData, serializedData)

    val deserializedData = Nutrient.deserialize(serializedData)
    Assert.assertEquals(nutrient, deserializedData)
  }

  @Test
  fun `illegal serialization arguments throw exception`() {
    assertFailsWith<Exception> {
      Nutrient.deserialize(mapOf("amount" to 100.0, "unit" to "INVALID"))
    }
  }
}
