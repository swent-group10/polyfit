package com.github.se.polyfit.ui.utils

fun removeLeadingZerosAndNonDigits(input: String): String {
  val onlyDigits = input.replace(Regex("[^\\d]"), "")

  return if (onlyDigits != "0" && onlyDigits.startsWith("0")) {
    onlyDigits.trimStart('0').ifEmpty { "0" }
  } else {
    onlyDigits
  }
}

fun titleCase(input: String): String {
  return input.split(" ").joinToString(" ") { word ->
    val smallCaseWord = word.lowercase()
    smallCaseWord.replaceFirstChar(Char::titlecaseChar)
  }
}
