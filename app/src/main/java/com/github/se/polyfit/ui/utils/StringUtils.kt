package com.github.se.polyfit.ui.utils

fun removeLeadingZerosAndNonDigits(input: String): String {
  // Remove all non-digits and non-dot characters, preserving the first dot encountered
  var dotFound = false
  val filteredInput =
      input.filter { char ->
        when {
          char.isDigit() -> true
          char == '.' && !dotFound -> {
            dotFound = true
            true
          }
          else -> false
        }
      }

  // Find the index of the first digit that is not 0 or the first occurrence of '.'
  val firstNonZeroDigitOrDotIndex =
      filteredInput.indexOfFirst { it != '0' || it == '.' }.takeIf { it != -1 } ?: return "0"

  // Extract the substring from the first non-zero digit or dot to the end
  val result = filteredInput.substring(firstNonZeroDigitOrDotIndex)

  // Handle cases where the result is only "." or starts with "."
  return when {
    result.isEmpty() || result == "." -> "0"
    result.startsWith('.') -> "0$result" // Prefix a leading zero if it starts with a dot
    else -> result
  }
}

fun titleCase(input: String): String {
  return input.split(" ").joinToString(" ") { word ->
    val smallCaseWord = word.lowercase()
    smallCaseWord.replaceFirstChar(Char::titlecaseChar)
  }
}
