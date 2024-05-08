package com.github.se.polyfit.model.meal

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class MealTagColor(val color: Color) {
  YELLOW(Color(0xFFFAEDCB)),
  GREEN(Color(0xFFC9E4DE)),
  TURQUOISE(Color(0xFFA8D1D1)),
  BLUE(Color(0xFFC6DEF1)),
  PURPLE(Color(0xFFDBCDF0)),
  PINK(Color(0xFFF2C6DE)),
  ORANGE(Color(0xFFFFD6A5)),
  RED(Color(0xFFFFADAD)),
  LIGHTGREEN(Color(0xFFE4F1EE)),
  WATERMELON(Color(0xFFFFCBCB)),
  LAVENDER(Color(0xFFBDB2FF)),
  BRIGHTORANGE(Color(0xFFFEC868)),
  BABYPINK(Color(0xFFFAD1FA)),
  SOFTPEACH(Color(0xFFFEDCD2)),
  LIGHTBLUE(Color(0xFFB5EAD7)),
  MINTGREEN(Color(0xFFA2D5AB)),
  PEACH(Color(0xFFFFDAC1)),
  ICEBLUE(Color(0xFFDAF4F0)),
  CORAL(Color(0xFFFFB7A5)),
  TEAL(Color(0xFFAFDBD2)),
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
        require(mealTag.isComplete()) {
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
