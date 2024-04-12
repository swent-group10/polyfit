package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before

class NutritionalInformationTest {
  private lateinit var nutritionalInformation: NutritionalInformation

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0

    nutritionalInformation =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL)))
  }

  @Test
  fun testSerialize() {
    val serializedData = NutritionalInformation.serialize(nutritionalInformation)
    val expectedData =
        listOf(
            mapOf(
                "nutrientType" to "totalWeight",
                "amount" to 100.0,
                "unit" to MeasurementUnit.G.toString()),
            mapOf(
                "nutrientType" to "calories",
                "amount" to 200.0,
                "unit" to MeasurementUnit.CAL.toString()),
        )

    assertEquals(expectedData, serializedData)
  }

  @Test
  fun testDeserialize() {
    val serializedData = NutritionalInformation.serialize(nutritionalInformation)
    val deserializedData = NutritionalInformation.deserialize(serializedData)
    assertTrue { nutritionalInformation.equals(deserializedData) }
  }

  @Test
  fun `test if two different nutritional information are equal`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 9.0, MeasurementUnit.G)))

    assertNotEquals(nutritionalInformation1, nutritionalInformation2)
  }

  @Test
  fun `fault serialization should return null`() {
    val wrongData =
        listOf(
            mapOf("nutrientType" to "totalWeight", "amount" to 100.0, "unit" to "G"),
            mapOf("nutrientType" to "calories", "amount" to 200.0, "unit" to "CAL"),
            mapOf("nutrientType" to "fat", "amount" to 10.0, "unit" to "G"),
            mapOf("nutrientType" to "saturatedFat", "amount" to 3.0, "unit" to "G"),
            mapOf("nutrientType" to "carbohydrates", "amount" to 20.0, "unit" to "G"),
            mapOf("nutrientType" to "netCarbohydrates", "amount" to 15.0, "unit" to "G"),
            mapOf("nutrientType" to "sugar", "amount" to 5.0, "unit" to "G"),
            mapOf("nutrientType" to "cholesterol", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "sodium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "protein", "amount" to 15.0, "unit" to "G"),
            mapOf("nutrientType" to "vitaminC", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "manganese", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "fiber", "amount" to 2.0, "unit" to "G"),
            mapOf("nutrientType" to "vitaminB6", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "copper", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "vitaminB1", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "folate", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "potassium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "magnesium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "vitaminB3", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "vitaminB5", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "vitaminB2", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "iron", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "calcium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "vitaminA", "amount" to 0.0, "unit" to "IU"),
            mapOf("nutrientType" to "zinc", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "phosphorus", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "vitaminK", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "selenium", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "vitaminE", "amount" to 0, "unit" to "INVALID"))

    assertFailsWith<Exception> { NutritionalInformation.deserialize(wrongData) }
  }

  @Test
  fun `plus operator should add nutritional information correctly`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 50.0, MeasurementUnit.G),
                Nutrient("calories", 100.0, MeasurementUnit.CAL),
                Nutrient("fat", 5.0, MeasurementUnit.G)))

    val result = nutritionalInformation1 + nutritionalInformation2

    assertEquals(
        Nutrient("totalWeight", 150.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "totalWeight" }[0])
    assertEquals(
        Nutrient("calories", 300.0, MeasurementUnit.CAL),
        result.nutrients.filter { it.nutrientType == "calories" }[0])
    assertEquals(
        Nutrient("fat", 15.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "fat" }[0])
  }

  @Test
  fun `minus operator should add nutritional information correctly`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 50.0, MeasurementUnit.G),
                Nutrient("calories", 100.0, MeasurementUnit.CAL),
                Nutrient("fat", 5.0, MeasurementUnit.G)))

    val result = nutritionalInformation1 - nutritionalInformation2

    assertEquals(
        Nutrient("totalWeight", 50.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "totalWeight" }[0])
    assertEquals(
        Nutrient("calories", 100.0, MeasurementUnit.CAL),
        result.nutrients.filter { it.nutrientType == "calories" }[0])
    assertEquals(
        Nutrient("fat", 5.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "fat" }[0])
  }

  @Test
  fun `update function should correctly update nutritional information`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    val nutrient1 = Nutrient("totalWeight", 50.0, MeasurementUnit.G)

    nutritionalInformation1.update(nutrient1)

    assertEquals(
        Nutrient("totalWeight", 150.0, MeasurementUnit.G),
        nutritionalInformation1.nutrients.filter { it.nutrientType == "totalWeight" }[0])
  }

  @Test
  fun `equals function should correctly compare nutritional information`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    // Check if two identical instances are equal
    assertEquals(nutritionalInformation1, nutritionalInformation2)

    // Modify one instance
    nutritionalInformation2.update(Nutrient("totalWeight", 50.0, MeasurementUnit.G))

    // Check if two different instances are not equal
    assertNotEquals(nutritionalInformation1, nutritionalInformation2)
  }

  @Test
  fun `update function should not modify the original nutritional information`() {
    val originalNutritionalInformation =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    val newValues =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 50.0, MeasurementUnit.G),
                Nutrient("calories", 100.0, MeasurementUnit.CAL),
                Nutrient("fat", 5.0, MeasurementUnit.G)))

    val updatedNutritionalInformation = originalNutritionalInformation.deepCopy()

    updatedNutritionalInformation.update(newValues)

    assertNotEquals(originalNutritionalInformation, updatedNutritionalInformation)
  }

  @Test
  fun `test that the hash functions correctly`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("totalWeight", 100.0, MeasurementUnit.G),
                Nutrient("calories", 200.0, MeasurementUnit.CAL),
                Nutrient("fat", 10.0, MeasurementUnit.G)))

    assertEquals(nutritionalInformation1.hashCode(), nutritionalInformation2.hashCode())
  }

  @Test
  fun `getNutrient returns the correct nutrient`() {
    val nutrient = nutritionalInformation.getNutrient("totalWeight")
    assertEquals(Nutrient("totalWeight", 100.0, MeasurementUnit.G), nutrient)
  }

  @Test
  fun `getNutrient returns null if nutrient does not exist`() {
    val nutrient = nutritionalInformation.getNutrient("nonExistentNutrient")
    assertEquals(null, nutrient)
  }

  @Test
  fun `get certain amount of nutrient`() {
    val nutrient = nutritionalInformation.getNutrient("totalWeight")
    assertEquals(Nutrient("totalWeight", 100.0, MeasurementUnit.G), nutrient)
  }
}
