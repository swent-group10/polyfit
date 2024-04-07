package com.github.se.polyfit.model.meal

import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.junit.Assert.assertEquals

class MealOccasionTest {
  @Test
  fun fromString_withValidString_returnsCorrespondingMealOccasion() {
    assertEquals(MealOccasion.BREAKFAST, MealOccasion.fromString("BREAKFAST"))
    assertEquals(MealOccasion.LUNCH, MealOccasion.fromString("LUNCH"))
    assertEquals(MealOccasion.DINNER, MealOccasion.fromString("DINNER"))
    assertEquals(MealOccasion.SNACK, MealOccasion.fromString("SNACK"))
  }

  @Test
  fun fromString_withInvalidString_throwsException() {
    assertFailsWith<IllegalArgumentException> { MealOccasion.fromString("INVALID") }
  }

  @Test
  fun fromString_withLowercaseString_throwsException() {
    assertFailsWith<IllegalArgumentException> { MealOccasion.fromString("breakfast") }
  }
}
