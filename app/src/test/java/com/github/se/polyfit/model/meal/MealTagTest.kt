package com.github.se.polyfit.model.meal

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MealTagTest {
  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun testEquality() {
    val tag1 = MealTag("Breakfast", MealTagColor.PINK)
    val tag2 = MealTag("Breakfast", MealTagColor.PINK)
    val tag3 = MealTag("Lunch", MealTagColor.BLUE)

    assertEquals(tag1, tag2)
    assertNotEquals(tag1, tag3)
  }

  @Test
  fun testHashCode() {
    val tag = MealTag("Dinner", MealTagColor.PINK)
    val expectedHashCode = 31 * "Dinner".hashCode() + MealTagColor.PINK.hashCode()
    assertEquals(expectedHashCode, tag.hashCode())
  }

  @Test
  fun testSetTagName() {
    val tag = MealTag("OldName", MealTagColor.PINK)
    tag.tagName = "NewName"
    assertEquals("NewName", tag.tagName)
  }

  @Test
  fun testSetTagColor() {
    val tag = MealTag("Dinner", MealTagColor.PINK)
    tag.tagColor = MealTagColor.ORANGE
    assertEquals(MealTagColor.ORANGE, tag.tagColor)
  }

  @Test
  fun testIsComplete() {
    val tag = MealTag("Dinner", MealTagColor.PINK)
    assertTrue(tag.isComplete())
  }

  @Test
  fun testIsCompleteNegative() {
    val tag = MealTag("", MealTagColor.UNDEFINED)
    assertFalse(tag.isComplete())
  }

  @Test
  fun testSerialize() {
    val tag = MealTag("Snack", MealTagColor.BLUE)

    val expectedMap = mapOf("tagName" to "Snack", "tagColor" to "-3744015")

    assertEquals(expectedMap, tag.serialize())
  }

  @Test
  fun testDeserialize() {
    val map = mapOf<String, Any>("tagName" to "Snack", "tagColor" to "-3744015")
    val tag = MealTag.deserialize(map)

    assertEquals("Snack", tag.tagName)
    assertEquals(MealTagColor.BLUE, tag.tagColor)
  }

  @Test(expected = IllegalArgumentException::class)
  fun testDeserializeInvalidData() {
    val map = mapOf<String, Any>("tagName" to "Snack")
    MealTag.deserialize(map)
  }
}
