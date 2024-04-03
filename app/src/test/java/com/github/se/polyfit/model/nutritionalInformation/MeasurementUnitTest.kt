package com.github.se.polyfit.model.nutritionalInformation

import junit.framework.TestCase.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith

class MeasurementUnitTest {

  @Test
  fun `unitConversion converts grams to milligrams`() {
    val result = MeasurementUnit.unitConversion(MeasurementUnit.G, MeasurementUnit.MG, 1.0)
    assertEquals(1000.0, result)
  }

  @Test
  fun `unitConversion converts grams to micrograms`() {
    val result = MeasurementUnit.unitConversion(MeasurementUnit.G, MeasurementUnit.UG, 1.0)
    assertEquals(1000000.0, result)
  }

  @Test
  fun `unitConversion converts milligrams to grams`() {
    val result = MeasurementUnit.unitConversion(MeasurementUnit.MG, MeasurementUnit.G, 1000.0)
    assertEquals(1.0, result)
  }

  @Test
  fun `unitConversion converts milligrams to micrograms`() {
    val result = MeasurementUnit.unitConversion(MeasurementUnit.MG, MeasurementUnit.UG, 1.0)
    assertEquals(1000.0, result)
  }

  @Test
  fun `unitConversion converts micrograms to grams`() {
    val result = MeasurementUnit.unitConversion(MeasurementUnit.UG, MeasurementUnit.G, 1000000.0)
    assertEquals(1.0, result)
  }

  @Test
  fun `unitConversion converts micrograms to milligrams`() {
    val result = MeasurementUnit.unitConversion(MeasurementUnit.UG, MeasurementUnit.MG, 1000.0)
    assertEquals(1.0, result)
  }

  @Test
  fun `unitConversion returns same value for unsupported conversions`() {
    val result = MeasurementUnit.unitConversion(MeasurementUnit.IU, MeasurementUnit.CAL, 1.0)
    assertEquals(1.0, result)
  }

  @Test
  fun `illegal arguments throw exception`() {
    assertFailsWith<IllegalArgumentException> { MeasurementUnit.fromString("INVALID") }
  }
}
