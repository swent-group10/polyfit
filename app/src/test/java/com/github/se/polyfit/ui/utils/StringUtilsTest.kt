package com.github.se.polyfit.utils

import com.github.se.polyfit.ui.utils.removeLeadingZerosAndNonDigits
import com.github.se.polyfit.ui.utils.titleCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StringUtilsTest {
  // Tests for removeLeadingZerosAndNonDigits
  @Test
  fun `removeLeadingZeros and non-digits - all digits`() {
    val result = removeLeadingZerosAndNonDigits("001234")
    assertEquals("1234", result)
  }

  @Test
  fun `removeLeadingZeros and non-digits - leading zeros and non-digits`() {
    val result = removeLeadingZerosAndNonDigits("00a1b2c34")
    assertEquals("1234", result)
  }

  @Test
  fun `removeLeadingZeros and non-digits - only non-digits`() {
    val result = removeLeadingZerosAndNonDigits("abc")
    assertEquals("", result)
  }

  @Test
  fun `removeLeadingZeros and non-digits - mixed with leading non-digit chars`() {
    val result = removeLeadingZerosAndNonDigits("abc001234")
    assertEquals("1234", result)
  }

  @Test
  fun `removeLeadingZeros and non-digits - single zero`() {
    val result = removeLeadingZerosAndNonDigits("0")
    assertEquals("0", result)
  }

  @Test
  fun `removeLeadingZeros and non-digits - only zeros`() {
    val result = removeLeadingZerosAndNonDigits("0000")
    assertEquals("0", result)
  }

  @Test
  fun `removeLeadingZeros and non-digits - empty string`() {
    val result = removeLeadingZerosAndNonDigits("")
    assertEquals("", result)
  }

  @Test
  fun `removeLeadingZeros and non-digits - zeros and non-digits`() {
    val result = removeLeadingZerosAndNonDigits("00abc")
    assertEquals("0", result)
  }

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
