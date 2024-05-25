package com.github.se.polyfit.data.api

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class RecipeInstructionResponseAPI(
    val status: APIResponse,
    val recipes: List<RecipeInstruction>?,
    val returnedCode: Int = 200
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
data class RecipeInstruction(val name: String?, val steps: List<Step>)

@JsonClass(generateAdapter = true)
data class Step(
    val number: Int,
    val step: String,
    val ingredients: List<Ingredient>?,
    val equipment: List<Equipment>?,
    val length: TimeLength?
)

@JsonClass(generateAdapter = true)
data class Ingredient(val id: Int, val name: String, val localizedName: String, val image: String?)

@JsonClass(generateAdapter = true)
data class Equipment(
    val id: Int,
    val name: String,
    val localizedName: String,
    val image: String?,
    val temperature: Temperature?
)

@JsonClass(generateAdapter = true) data class Temperature(val number: Int, val unit: String)

@JsonClass(generateAdapter = true) data class TimeLength(val number: Int, val unit: String)
