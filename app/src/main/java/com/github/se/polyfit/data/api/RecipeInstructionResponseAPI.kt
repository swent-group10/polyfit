package com.github.se.polyfit.data.api

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class RecipeInstructionResponseAPI(
    val recipes: List<RecipeInstruction>?,
) {
  companion object {
    private val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    fun fromJson(jsonString: String): RecipeInstruction {
      val jsonAdapter = moshi.adapter(RecipeInstruction::class.java).lenient()
      return jsonAdapter.fromJson(jsonString) ?: throw IllegalArgumentException("Invalid JSON data")
    }
  }
}

@JsonClass(generateAdapter = true)
data class RecipeInstruction(val name: String?, val steps: List<Step>) {
  companion object {
    fun faillure(): RecipeInstruction {
      return RecipeInstruction(name = "", steps = emptyList())
    }
  }
}

@JsonClass(generateAdapter = true)
data class Step(
    val number: Int,
    val step: String,
    val ingredients: List<Ingredient>?,
    val equipment: List<Equipment>?,
    val length: TimeLength?
)

/**
 * Represents an ingredient in a meal. This has the same name as the data in the model package. This
 * is due to moshi needing the names of the classes being the same as the names in the Json.
 */
@JsonClass(generateAdapter = true)
data class Ingredient(
    val id: Int,
    val name: String,
    val localizedName: String?,
    val image: String?
) {
  companion object {
    fun serialize(ingredient: Ingredient): Map<String, Any> {
      val map = mutableMapOf<String, Any>()
      map["id"] = ingredient.id
      map["name"] = ingredient.name
      map["localizedName"] = ingredient.localizedName ?: ""
      map["image"] = ingredient.image ?: ""
      return map
    }

    fun deserialize(map: Map<String, Any>): Ingredient {
      return try {

        Ingredient(
            map["id"] as Int,
            map["name"] as String,
            map["localizedName"] as String,
            map["image"] as String)
      } catch (e: Exception) {
        throw IllegalArgumentException("Failed to deserialize Ingredient object", e)
      }
    }
  }
}

@JsonClass(generateAdapter = true)
data class Equipment(
    val id: Int,
    val name: String,
    val localizedName: String?,
    val image: String?,
    val temperature: Temperature?
)

@JsonClass(generateAdapter = true) data class Temperature(val number: Int, val unit: String)

@JsonClass(generateAdapter = true) data class TimeLength(val number: Int, val unit: String)
