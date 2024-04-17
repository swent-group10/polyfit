package com.github.se.polyfit.model.meal

import android.util.Log
import androidx.compose.ui.graphics.Color
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
    val tag1 = MealTag("Breakfast", Color.Red)
    val tag2 = MealTag("Breakfast", Color.Red)
    val tag3 = MealTag("Lunch", Color.Blue)

    assertEquals(tag1, tag2)
    assertNotEquals(tag1, tag3)
  }

  @Test
  fun testHashCode() {
    val tag = MealTag("Dinner", Color.Green)
    val expectedHashCode = 31 * "Dinner".hashCode() + Color.Green.hashCode()
    assertEquals(expectedHashCode, tag.hashCode())
  }

  @Test
  fun testSetTagName() {
    val tag = MealTag("OldName", Color.Green)
    tag.tagName = "NewName"
    assertEquals("NewName", tag.tagName)
  }

  @Test
  fun testSetTagColor() {
    val tag = MealTag("Dinner", Color.Green)
    tag.tagColor = Color.Yellow
    assertEquals(Color.Yellow, tag.tagColor)
  }

  @Test
  fun testIsComplete() {
    val tag = MealTag("Dinner", Color.Green)
    assertTrue(tag.isComplete())
  }

  @Test
  fun testIsCompleteNegative() {
    val tag = MealTag("", Color.Unspecified)
    assertFalse(tag.isComplete())
  }

  @Test
  fun testSerialize() {
    val tag1 = MealTag("Snack", Color.Blue)
    val tag2 = MealTag("Snack", Color(-16776961))
    val tag3 = MealTag("Snack", Color(0, 0, 255, 255))
    val tag4 = MealTag("Snack", Color(0xFF0000FF))

    val expectedMap = mapOf("tagName" to "Snack", "tagColor" to "-16776961")

    assertEquals(expectedMap, tag1.serialize())
    assertEquals(expectedMap, tag2.serialize())
    assertEquals(expectedMap, tag3.serialize())
    assertEquals(expectedMap, tag4.serialize())
  }

  @Test
  fun testDeserialize() {
    val map = mapOf<String, Any>("tagName" to "Snack", "tagColor" to "-16776961")
    val tag = MealTag.deserialize(map)

    assertEquals("Snack", tag.tagName)
    assertEquals(Color.Blue, tag.tagColor)
  }

  @Test(expected = IllegalArgumentException::class)
  fun testDeserializeInvalidData() {
    val map = mapOf<String, Any>("tagName" to "Snack")
    MealTag.deserialize(map)
  }
}
