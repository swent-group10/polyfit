package com.github.se.polyfit.model.meal

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class MealTagColor(val color: Color) {
  BLUE(Color(0xFFC6DEF1)),
  ORANGE(Color(0xFFFFD6A5)),
  PINK(Color(0xFFFAD1FA)),
  RED(Color(0xFFFFADAD)),
  BRIGHTORANGE(Color(0xFFFEC868)),
  LIGHTBLUE(Color(0xFFB5EAD7)),
  MINTGREEN(Color(0xFFA2D5AB)),
  LAVENDER(Color(0xFFC5A3FF)),
  PEACH(Color(0xFFFFDAC1)),
  BEIGE(Color(0xFFE2CFC4)),
  CORAL(Color(0xFFFFB7A5)),
  TEAL(Color(0xFFAFDBD2)),
  SKYBLUE(Color(0xFFAFCBFF)),
  SOFTYELLOW(Color(0xFFFEF9A7)),
  POWDERBLUE(Color(0xFFB6CCFE)),
  LIGHTLAVENDER(Color(0xFFECD4FF)),
  SOFTGREEN(Color(0xFFB9F8D3)),
  ROSE(Color(0xFFFFABD8)),
  IVORY(Color(0xFFFFFFF0)),
  LIGHTCORAL(Color(0xFFF88379)),
  UNDEFINED(Color.Unspecified); // Add more colors as needed

  companion object {
    fun fromArgb(argb: Int): MealTagColor {
      return entries.find { it.color.toArgb() == argb } ?: UNDEFINED
    }
  }
}

data class MealTag(var tagName: String, var tagColor: MealTagColor) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is MealTag) return false

    if (tagName != other.tagName) return false
    if (tagColor != other.tagColor) return false

    return true
  }

  override fun hashCode(): Int {
    var result = tagName.hashCode()
    result = 31 * result + tagColor.hashCode()

    return result
  }

  fun isComplete(): Boolean {
    return tagName.isNotEmpty() && tagColor != MealTagColor.UNDEFINED
  }

  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

  companion object {

    fun serialize(data: MealTag): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["tagName"] = data.tagName
        this["tagColor"] = data.tagColor.color.toArgb().toString()
      }
    }

    fun deserialize(data: Map<String, Any>): MealTag {
      return try {
        val tagName = data["tagName"] as String
        val tagColor = MealTagColor.fromArgb((data["tagColor"] as String).toInt())

        val mealTag = MealTag(tagName, tagColor)
        if (!mealTag.isComplete()) {
          throw IllegalArgumentException("MealTag object is incomplete")
        }
        mealTag
      } catch (e: Exception) {
        Log.e("MealTag", "Failed to deserialize MealTag object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize MealTag object", e)
      }
    }

    fun default(): MealTag {
      return MealTag(tagName = "", tagColor = MealTagColor.UNDEFINED)
    }
  }
}
