package com.github.se.polyfit.model.recipe

import android.util.Log
import java.net.URL

data class Recipe(
    val id: Long,
    val title: String,
    val imageUrl: URL,
    val usedIngredients: Long,
    val missingIngredients: Long,
    val likes: Long,
    val recipeInformation: RecipeInformation,
    var firebaseId: String = ""
) {

  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

  companion object {
    fun serialize(recipe: Recipe): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["id"] = recipe.id
        this["title"] = recipe.title
        this["imageURL"] = recipe.imageUrl
        this["usedIngredients"] = recipe.usedIngredients
        this["missingIngredients"] = recipe.missingIngredients
        this["likes"] = recipe.likes
        this["recipeInformation"] = recipe.recipeInformation.serialize()
      }
    }

    fun deserialize(data: Map<String, Any>): Recipe {
      return try {
        val id = data["id"] as Long
        val title = data["title"] as String
        val imageUrl = data["imageURL"] as URL
        val usedIngredients = data["usedIngredients"] as Long
        val missingIngredients = data["missingIngredients"] as Long
        val likes = data["likes"] as Long
        val recipeInformation =
            RecipeInformation.deserialize(data["recipeInformation"] as Map<String, Any>)

        Recipe(id, title, imageUrl, usedIngredients, missingIngredients, likes, recipeInformation)
      } catch (e: Exception) {
        Log.e("Recipe", "Failed to deserialize Recipe object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Recipe object", e)
      }
    }
  }
}
