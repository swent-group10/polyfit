package com.github.se.polyfit.utils

import com.github.se.polyfit.ui.utils.titleCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StringUtilsTest {
  @Test
  fun titleCaseSingleLower() {
    val input = "hello"
    val expected = "Hello"
    val actual = titleCase(input)
    assertEquals(expected, actual)
  }

  @Test
  fun titleCaseSingleUpper() {
    val input = "HELLO"
    val expected = "Hello"
    val actual = titleCase(input)
    assertEquals(expected, actual)
  }

  @Test
  fun titleCaseMultipleWords() {
    val input = "hello world"
    val expected = "Hello World"
    val actual = titleCase(input)
    assertEquals(expected, actual)
  }

  @Test
  fun titleCaseMultipleWordsWithSpaces() {
    val input = "hello   world"
    val expected = "Hello   World"
    val actual = titleCase(input)
    assertEquals(expected, actual)
  }
}
