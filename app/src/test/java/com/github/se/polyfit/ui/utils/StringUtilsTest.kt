package com.github.se.polyfit.utils

import com.github.se.polyfit.ui.utils.removeLeadingZerosAndNonDigits
import com.github.se.polyfit.ui.utils.splitStringByCharacter
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

  // Tests for splitStringByCharacter
  @Test
  fun `splits normal string by given character`() {
    val result = splitStringByCharacter("hello world", ' ')
    assertEquals(listOf("hello", "world"), result)
  }

  @Test
  fun `splits string with multiple delimiters`() {
    val result = splitStringByCharacter("a-b--c", '-')
    assertEquals(listOf("a", "b", "c"), result)
  }

  @Test
  fun `returns empty list when input is empty`() {
    val result = splitStringByCharacter("", ' ')
    assertEquals(emptyList<String>(), result)
  }

  @Test
  fun `returns list with input when delimiter is not found`() {
    val result = splitStringByCharacter("hello", ' ')
    assertEquals(listOf("hello"), result)
  }

  @Test
  fun `splits string with delimiter at the start`() {
    val result = splitStringByCharacter("-start", '-')
    assertEquals(listOf("start"), result)
  }

  @Test
  fun `splits string with delimiter at the end`() {
    val result = splitStringByCharacter("end-", '-')
    assertEquals(listOf("end"), result)
  }

  @Test
  fun `ignores empty strings between consecutive delimiters`() {
    val result = splitStringByCharacter("ab--cd", '-')
    assertEquals(listOf("ab", "cd"), result)
  }

  @Test
  fun `handles string with only delimiters`() {
    val result = splitStringByCharacter("----", '-')
    assertEquals(emptyList<String>(), result)
  }
}
