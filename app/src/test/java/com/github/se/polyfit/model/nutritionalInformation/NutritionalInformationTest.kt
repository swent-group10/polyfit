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
    every { Log.d(any(), any()) } returns 0

    nutritionalInformation =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL)))
  }

  @Test
  fun testSerialize() {
    val serializedData = NutritionalInformation.serialize(nutritionalInformation)
    val expectedData =
        listOf(
            mapOf(
                "nutrientType" to "Total Weight",
                "amount" to 100.0,
                "unit" to MeasurementUnit.G.toString()),
            mapOf(
                "nutrientType" to "Calories",
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
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 9.0, MeasurementUnit.G)))

    assertNotEquals(nutritionalInformation1, nutritionalInformation2)
  }

  @Test
  fun `fault serialization should return null`() {
    val wrongData =
        listOf(
            mapOf("nutrientType" to "Total Weight", "amount" to 100.0, "unit" to "G"),
            mapOf("nutrientType" to "Calories", "amount" to 200.0, "unit" to "CAL"),
            mapOf("nutrientType" to "Fat", "amount" to 10.0, "unit" to "G"),
            mapOf("nutrientType" to "Saturated Fat", "amount" to 3.0, "unit" to "G"),
            mapOf("nutrientType" to "Carbohydrates", "amount" to 20.0, "unit" to "G"),
            mapOf("nutrientType" to "Net Carbohydrates", "amount" to 15.0, "unit" to "G"),
            mapOf("nutrientType" to "Sugar", "amount" to 5.0, "unit" to "G"),
            mapOf("nutrientType" to "Cholesterol", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Sodium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Protein", "amount" to 15.0, "unit" to "G"),
            mapOf("nutrientType" to "Vitamin C", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "manganese", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "Fiber", "amount" to 2.0, "unit" to "G"),
            mapOf("nutrientType" to "Vitamin B6", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Copper", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "Vitamin B1", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Folate", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "Potassium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Magnesium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Vitamin B3", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Vitamin B5", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Vitamin B2", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Iron", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Calcium", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Vitamin A", "amount" to 0.0, "unit" to "IU"),
            mapOf("nutrientType" to "Zinc", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Phosphorus", "amount" to 0.0, "unit" to "MG"),
            mapOf("nutrientType" to "Vitamin K", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "Selenium", "amount" to 0.0, "unit" to "UG"),
            mapOf("nutrientType" to "Vitamin E", "amount" to 0, "unit" to "INVALID"))

    assertFailsWith<Exception> { NutritionalInformation.deserialize(wrongData) }
  }

  @Test
  fun `plus operator should add nutritional information correctly`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 50.0, MeasurementUnit.G),
                Nutrient("Calories", 100.0, MeasurementUnit.CAL),
                Nutrient("Fat", 5.0, MeasurementUnit.G)))

    val result = nutritionalInformation1 + nutritionalInformation2

    assertEquals(
        Nutrient("Total Weight", 150.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "Total Weight" }[0])
    assertEquals(
        Nutrient("Calories", 300.0, MeasurementUnit.CAL),
        result.nutrients.filter { it.nutrientType == "Calories" }[0])
    assertEquals(
        Nutrient("Fat", 15.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "Fat" }[0])
  }

  @Test
  fun `minus operator should add nutritional information correctly`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 50.0, MeasurementUnit.G),
                Nutrient("Calories", 100.0, MeasurementUnit.CAL),
                Nutrient("Fat", 5.0, MeasurementUnit.G)))

    val result = nutritionalInformation1 - nutritionalInformation2

    assertEquals(
        Nutrient("Total Weight", 50.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "Total Weight" }[0])
    assertEquals(
        Nutrient("Calories", 100.0, MeasurementUnit.CAL),
        result.nutrients.filter { it.nutrientType == "Calories" }[0])
    assertEquals(
        Nutrient("Fat", 5.0, MeasurementUnit.G),
        result.nutrients.filter { it.nutrientType == "Fat" }[0])
  }

  @Test
  fun `minus function should remove a Nutrient if it goes negative`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL)))
    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 50.0, MeasurementUnit.G),
                Nutrient("Calories", 200.1, MeasurementUnit.CAL)))

    val result = nutritionalInformation1 - nutritionalInformation2
    assert(result.nutrients.size == 1)
    assert(result.nutrients[0].nutrientType == "Total Weight")
  }

  @Test
  fun `update function should correctly update nutritional information`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    val nutrient1 = Nutrient("Total Weight", 50.0, MeasurementUnit.G)

    nutritionalInformation1.update(nutrient1)

    assertEquals(
        Nutrient("Total Weight", 150.0, MeasurementUnit.G),
        nutritionalInformation1.nutrients.filter { it.nutrientType == "Total Weight" }[0])
  }

  @Test
  fun `update shouldn't add a nutrient if the amount is less than 0`() {
    val nutritionalInformation1 = NutritionalInformation(mutableListOf())
    val nutritionalInformation2 =
        NutritionalInformation(mutableListOf(Nutrient("Total Weight", -10.0, MeasurementUnit.G)))

    nutritionalInformation1.update(nutritionalInformation2)
    assert(nutritionalInformation1.nutrients.isEmpty())
  }

  @Test
  fun `update should subtract from existing nutrient if amount is less than 0`() {
    val nutritionalInformation1 =
        NutritionalInformation(mutableListOf(Nutrient("Total Weight", 10.0, MeasurementUnit.G)))
    val nutritionalInformation2 =
        NutritionalInformation(mutableListOf(Nutrient("Total Weight", -5.0, MeasurementUnit.G)))

    nutritionalInformation1.update(nutritionalInformation2)
    assert(nutritionalInformation1.nutrients.size == 1)
    assert(nutritionalInformation1.nutrients[0].amount == 5.0)
  }

  @Test
  fun `equals function should correctly compare nutritional information`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    // Check if two identical instances are equal
    assertEquals(nutritionalInformation1, nutritionalInformation2)

    // Modify one instance
    nutritionalInformation2.update(Nutrient("Total Weight", 50.0, MeasurementUnit.G))

    // Check if two different instances are not equal
    assertNotEquals(nutritionalInformation1, nutritionalInformation2)
  }

  @Test
  fun `update function should not modify the original nutritional information`() {
    val originalNutritionalInformation =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    val newValues =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 50.0, MeasurementUnit.G),
                Nutrient("Calories", 100.0, MeasurementUnit.CAL),
                Nutrient("Fat", 5.0, MeasurementUnit.G)))

    val updatedNutritionalInformation = originalNutritionalInformation.deepCopy()

    updatedNutritionalInformation.update(newValues)

    assertNotEquals(originalNutritionalInformation, updatedNutritionalInformation)
  }

  @Test
  fun `test that the hash functions correctly`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    val nutritionalInformation2 =
        NutritionalInformation(
            mutableListOf(
                Nutrient("Total Weight", 100.0, MeasurementUnit.G),
                Nutrient("Calories", 200.0, MeasurementUnit.CAL),
                Nutrient("Fat", 10.0, MeasurementUnit.G)))

    assertEquals(nutritionalInformation1.hashCode(), nutritionalInformation2.hashCode())
  }

  @Test
  fun `getNutrient returns the correct nutrient`() {
    val nutrient = nutritionalInformation.getNutrient("Total Weight")
    assertEquals(Nutrient("Total Weight", 100.0, MeasurementUnit.G), nutrient)
  }

  @Test
  fun `getNutrient returns null if nutrient does not exist`() {
    val nutrient = nutritionalInformation.getNutrient("nonExistentNutrient")
    assertEquals(null, nutrient)
  }

  @Test
  fun `get certain total of nutrient`() {
    val nutrient = nutritionalInformation.calculateTotalNutrient("Total Weight")
    assertEquals(100.0, nutrient, 0.1)
  }

  @Test
  fun `get certain amount of nutrient`() {
    val nutrient = nutritionalInformation.getNutrient("Total Weight")
    assertEquals(Nutrient("Total Weight", 100.0, MeasurementUnit.G), nutrient)
  }
}
