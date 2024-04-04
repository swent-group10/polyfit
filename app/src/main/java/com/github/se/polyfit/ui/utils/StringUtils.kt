package com.github.se.polyfit.ui.utils

fun titleCase(input: String): String {
  return input.split(" ").joinToString(" ") { word ->
    val smallCaseWord = word.lowercase()
    smallCaseWord.replaceFirstChar(Char::titlecaseChar)
  }
}
