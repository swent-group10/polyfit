package com.github.se.polyfit.data.api

import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.model.recipe.RecipeInformation
import java.net.URL
import org.json.JSONArray

data class RecipeFromIngredientsResponseAPI(
    val status: APIResponse,
    val recipes: List<Recipe>,
    val returnedCode: Int = 200
) {
  companion object {
    fun faillure(): RecipeFromIngredientsResponseAPI {
      return RecipeFromIngredientsResponseAPI(status = APIResponse.FAILURE, recipes = emptyList())
    }

    fun fromJsonObject(jsonArray: JSONArray): RecipeFromIngredientsResponseAPI {
      try {
        val recipes = mutableListOf<Recipe>()
        for (i in 0 until jsonArray.length()) {
          val currObject = jsonArray.getJSONObject(i)

          val id = currObject.getLong("id")
          val title = currObject.getString("title")
          val image = currObject.getString("image")
          val likes = currObject.getString("likes").toLong()
          val usedIngredientCount = currObject.getString("usedIngredientCount").toLong()
          val missedIngredientCount = currObject.getString("missedIngredientCount").toLong()

          recipes.add(
              Recipe(
                  id = id,
                  title = title,
                  imageUrl = URL(image),
                  likes = likes,
                  missingIngredients = missedIngredientCount,
                  usedIngredients = usedIngredientCount,
                  recipeInformation = RecipeInformation.default()))
        }

        return RecipeFromIngredientsResponseAPI(status = APIResponse.SUCCESS, recipes = recipes)
      } catch (e: Exception) {
        return faillure()
      }
    }
  }
}
