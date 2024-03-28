package com.github.se.polyfit.model.nutritionalInformation


import org.junit.Assert.assertEquals
import org.junit.Test

class NutritionalInformationTest {
    private val nutritionalInformation = NutritionalInformation(
        totalWeight = NutritionalInformation.Nutrient(100.0, MeasurementUnit.G),
        calories = NutritionalInformation.Nutrient(200.0, MeasurementUnit.CAL),
        fat = NutritionalInformation.Nutrient(10.0, MeasurementUnit.G),
        saturatedFat = NutritionalInformation.Nutrient(3.0, MeasurementUnit.G),
        carbohydrates = NutritionalInformation.Nutrient(20.0, MeasurementUnit.G),
        netCarbohydrates = NutritionalInformation.Nutrient(15.0, MeasurementUnit.G),
        sugar = NutritionalInformation.Nutrient(5.0, MeasurementUnit.G),
        cholesterol = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        sodium = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        protein = NutritionalInformation.Nutrient(15.0, MeasurementUnit.G),
        vitaminC = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        manganese = NutritionalInformation.Nutrient(0.0, MeasurementUnit.UG),
        fiber = NutritionalInformation.Nutrient(2.0, MeasurementUnit.G),
        vitaminB6 = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        copper = NutritionalInformation.Nutrient(0.0, MeasurementUnit.UG),
        vitaminB1 = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        folate = NutritionalInformation.Nutrient(0.0, MeasurementUnit.UG),
        potassium = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        magnesium = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        vitaminB3 = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        vitaminB5 = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        vitaminB2 = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        iron = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        calcium = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        vitaminA = NutritionalInformation.Nutrient(0.0, MeasurementUnit.IU),
        zinc = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        phosphorus = NutritionalInformation.Nutrient(0.0, MeasurementUnit.MG),
        vitaminK = NutritionalInformation.Nutrient(0.0, MeasurementUnit.UG),
        selenium = NutritionalInformation.Nutrient(0.0, MeasurementUnit.UG),
        vitaminE = NutritionalInformation.Nutrient(0.0, MeasurementUnit.IU)
    )

    @Test
    fun testSerialize() {
        val serializedData = NutritionalInformation.serialize(nutritionalInformation)
        val expectedData = mapOf(
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
            "vitaminE" to mapOf("amount" to 0.0, "unit" to "IU")

        )
        assertEquals(expectedData, serializedData)
    }

    @Test
    fun testDeserialize() {
        val serializedData = NutritionalInformation.serialize(nutritionalInformation)
        val deserializedData = NutritionalInformation.deserialize(serializedData)
        assertEquals(nutritionalInformation, deserializedData)
    }
}