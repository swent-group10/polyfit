package com.github.se.polyfit.model.nutritionalInformation

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class NutritionalInformationTest {
  private lateinit var nutritionalInformation: NutritionalInformation

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0

    nutritionalInformation =
        NutritionalInformation(
            totalWeight = Nutrient(100.0, MeasurementUnit.G),
            calories = Nutrient(200.0, MeasurementUnit.CAL),
            fat = Nutrient(10.0, MeasurementUnit.G),
            saturatedFat = Nutrient(3.0, MeasurementUnit.G),
            carbohydrates = Nutrient(20.0, MeasurementUnit.G),
            netCarbohydrates = Nutrient(15.0, MeasurementUnit.G),
            sugar = Nutrient(5.0, MeasurementUnit.G),
            cholesterol = Nutrient(0.0, MeasurementUnit.MG),
            sodium = Nutrient(0.0, MeasurementUnit.MG),
            protein = Nutrient(15.0, MeasurementUnit.G),
            vitaminC = Nutrient(0.0, MeasurementUnit.MG),
            manganese = Nutrient(0.0, MeasurementUnit.UG),
            fiber = Nutrient(2.0, MeasurementUnit.G),
            vitaminB6 = Nutrient(0.0, MeasurementUnit.MG),
            copper = Nutrient(0.0, MeasurementUnit.UG),
            vitaminB1 = Nutrient(0.0, MeasurementUnit.MG),
            folate = Nutrient(0.0, MeasurementUnit.UG),
            potassium = Nutrient(0.0, MeasurementUnit.MG),
            magnesium = Nutrient(0.0, MeasurementUnit.MG),
            vitaminB3 = Nutrient(0.0, MeasurementUnit.MG),
            vitaminB5 = Nutrient(0.0, MeasurementUnit.MG),
            vitaminB2 = Nutrient(0.0, MeasurementUnit.MG),
            iron = Nutrient(0.0, MeasurementUnit.MG),
            calcium = Nutrient(0.0, MeasurementUnit.MG),
            vitaminA = Nutrient(0.0, MeasurementUnit.IU),
            zinc = Nutrient(0.0, MeasurementUnit.MG),
            phosphorus = Nutrient(0.0, MeasurementUnit.MG),
            vitaminK = Nutrient(0.0, MeasurementUnit.UG),
            selenium = Nutrient(0.0, MeasurementUnit.UG),
            vitaminE = Nutrient(0.0, MeasurementUnit.IU))
  }

  @Test
  fun testSerialize() {
    val serializedData = NutritionalInformation.serialize(nutritionalInformation).toSortedMap()
    val expectedData =
        mapOf(
                "totalWeight" to mapOf("amount" to 100.0, "unit" to "G"),
                "calories" to mapOf("amount" to 200.0, "unit" to "CAL"),
                "fat" to mapOf("amount" to 10.0, "unit" to "G"),
                "saturatedFat" to mapOf("amount" to 3.0, "unit" to "G"),
                "carbohydrates" to mapOf("amount" to 20.0, "unit" to "G"),
                "netCarbohydrates" to mapOf("amount" to 15.0, "unit" to "G"),
                "sugar" to mapOf("amount" to 5.0, "unit" to "G"),
                "cholesterol" to mapOf("amount" to 0.0, "unit" to "MG"),
                "sodium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "protein" to mapOf("amount" to 15.0, "unit" to "G"),
                "vitaminC" to mapOf("amount" to 0.0, "unit" to "MG"),
                "manganese" to mapOf("amount" to 0.0, "unit" to "UG"),
                "fiber" to mapOf("amount" to 2.0, "unit" to "G"),
                "vitaminB6" to mapOf("amount" to 0.0, "unit" to "MG"),
                "copper" to mapOf("amount" to 0.0, "unit" to "UG"),
                "vitaminB1" to mapOf("amount" to 0.0, "unit" to "MG"),
                "folate" to mapOf("amount" to 0.0, "unit" to "UG"),
                "potassium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "magnesium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminB3" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminB5" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminB2" to mapOf("amount" to 0.0, "unit" to "MG"),
                "iron" to mapOf("amount" to 0.0, "unit" to "MG"),
                "calcium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminA" to mapOf("amount" to 0.0, "unit" to "IU"),
                "zinc" to mapOf("amount" to 0.0, "unit" to "MG"),
                "phosphorus" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminK" to mapOf("amount" to 0.0, "unit" to "UG"),
                "selenium" to mapOf("amount" to 0.0, "unit" to "UG"),
                "vitaminE" to mapOf("amount" to 0.0, "unit" to "IU"))
            .toSortedMap()

    assertEquals(expectedData, serializedData)
  }

  @Test
  fun testDeserialize() {
    val serializedData = NutritionalInformation.serialize(nutritionalInformation)
    val deserializedData = NutritionalInformation.deserialize(serializedData)
    assertEquals(nutritionalInformation, deserializedData)
  }

  @Test
  fun testDeserializeWithWrongValues() {
    val wrongData =
        mapOf(
                "totalWeight" to mapOf("amount" to 100.0, "unit" to "asdfasdfasdf"),
                "calories" to mapOf("amount" to 200.0, "unit" to "CAL"),
                "fat" to mapOf("amount" to 10.0, "unit" to "G"),
                "saturatedFat" to mapOf("amount" to 3.0, "unit" to "G"),
                "carbohydrates" to mapOf("amount" to 20.0, "unit" to "G"),
                "netCarbohydrates" to mapOf("amount" to 15.0, "unit" to "G"),
                "sugar" to mapOf("amount" to 5.0, "unit" to "G"),
                "cholesterol" to mapOf("amount" to 0.0, "unit" to "MG"),
                "sodium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "protein" to mapOf("amount" to 15.0, "unit" to "G"),
                "vitaminC" to mapOf("amount" to 0.0, "unit" to "MG"),
                "manganese" to mapOf("amount" to 0.0, "unit" to "UG"),
                "fiber" to mapOf("amount" to 2.0, "unit" to "G"),
                "vitaminB6" to mapOf("amount" to 0.0, "unit" to "MG"),
                "copper" to mapOf("amount" to 0.0, "unit" to "UG"),
                "vitaminB1" to mapOf("amount" to 0.0, "unit" to "MG"),
                "folate" to mapOf("amount" to 0.0, "unit" to "UG"),
                "potassium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "magnesium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminB3" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminB5" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminB2" to mapOf("amount" to 0.0, "unit" to "MG"),
                "iron" to mapOf("amount" to 0.0, "unit" to "MG"),
                "calcium" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminA" to mapOf("amount" to 0.0, "unit" to "IU"),
                "zinc" to mapOf("amount" to 0.0, "unit" to "MG"),
                "phosphorus" to mapOf("amount" to 0.0, "unit" to "MG"),
                "vitaminK" to mapOf("amount" to 0.0, "unit" to "UG"),
                "selenium" to mapOf("amount" to 0.0, "unit" to "UG"),
                "vitaminE" to mapOf("amount" to 0.0, "unit" to "IU"))
            .toSortedMap()

    assertEquals(null, NutritionalInformation.deserialize(wrongData))
  }

  @Test
  fun `plus operator should add nutritional information correctly`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            totalWeight = Nutrient(100.0, MeasurementUnit.G),
            calories = Nutrient(200.0, MeasurementUnit.CAL),
            fat = Nutrient(10.0, MeasurementUnit.G))

    val nutritionalInformation2 =
        NutritionalInformation(
            totalWeight = Nutrient(50.0, MeasurementUnit.G),
            calories = Nutrient(100.0, MeasurementUnit.CAL),
            fat = Nutrient(5.0, MeasurementUnit.G))

    val result = nutritionalInformation1 + nutritionalInformation2

    assertEquals(Nutrient(150.0, MeasurementUnit.G), result.totalWeight)
    assertEquals(Nutrient(300.0, MeasurementUnit.CAL), result.calories)
    assertEquals(Nutrient(15.0, MeasurementUnit.G), result.fat)
  }

  @Test
  fun `update function should correctly update nutritional information`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            totalWeight = Nutrient(100.0, MeasurementUnit.G),
            calories = Nutrient(200.0, MeasurementUnit.CAL),
            fat = Nutrient(10.0, MeasurementUnit.G))

    val nutritionalInformation2 =
        NutritionalInformation(
            totalWeight = Nutrient(50.0, MeasurementUnit.G),
            calories = Nutrient(100.0, MeasurementUnit.CAL),
            fat = Nutrient(5.0, MeasurementUnit.G))

    nutritionalInformation1.update(nutritionalInformation2)

    assertEquals(Nutrient(50.0, MeasurementUnit.G), nutritionalInformation1.totalWeight)
    assertEquals(Nutrient(100.0, MeasurementUnit.CAL), nutritionalInformation1.calories)
    assertEquals(Nutrient(5.0, MeasurementUnit.G), nutritionalInformation1.fat)
  }

  @Test
  fun `equals function should correctly compare nutritional information`() {
    val nutritionalInformation1 =
        NutritionalInformation(
            totalWeight = Nutrient(100.0, MeasurementUnit.G),
            calories = Nutrient(200.0, MeasurementUnit.CAL),
            fat = Nutrient(10.0, MeasurementUnit.G))

    val nutritionalInformation2 =
        NutritionalInformation(
            totalWeight = Nutrient(100.0, MeasurementUnit.G),
            calories = Nutrient(200.0, MeasurementUnit.CAL),
            fat = Nutrient(10.0, MeasurementUnit.G))

    // Check if two identical instances are equal
    assertEquals(nutritionalInformation1, nutritionalInformation2)

    // Modify one instance
    nutritionalInformation2.totalWeight = Nutrient(50.0, MeasurementUnit.G)

    // Check if two different instances are not equal
    assertNotEquals(nutritionalInformation1, nutritionalInformation2)
  }

  @Test
  fun `update function should not modify the original nutritional information`() {
    val originalNutritionalInformation =
        NutritionalInformation(
            totalWeight = Nutrient(100.0, MeasurementUnit.G),
            calories = Nutrient(200.0, MeasurementUnit.CAL),
            fat = Nutrient(10.0, MeasurementUnit.G))

    val newValues =
        NutritionalInformation(
            totalWeight = Nutrient(50.0, MeasurementUnit.G),
            calories = Nutrient(100.0, MeasurementUnit.CAL),
            fat = Nutrient(5.0, MeasurementUnit.G))

    val updatedNutritionalInformation = originalNutritionalInformation.deepCopy()
    updatedNutritionalInformation.update(newValues)

    assertNotEquals(originalNutritionalInformation, updatedNutritionalInformation)
  }
}
