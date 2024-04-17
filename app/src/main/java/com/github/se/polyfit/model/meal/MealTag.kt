package com.github.se.polyfit.model.meal

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class MealTag(var tagName: String, var tagColor: Color) {
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
    return tagName.isNotEmpty() && tagColor != Color.Unspecified
  }

  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

  companion object {

    fun serialize(data: MealTag): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["tagName"] = data.tagName
        this["tagColor"] = data.tagColor.toArgb().toString()
      }
    }

    fun deserialize(data: Map<String, Any>): MealTag {
      return try {
        val tagName = data["tagName"] as String
        val tagColor = Color((data["tagColor"] as String).toInt())

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
      return MealTag(tagName = "", tagColor = Color.Unspecified)
    }
  }
}
