package com.github.se.polyfit.data.api.Spoonacular

import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.model.recipe.RecipeInformation
import java.net.URL
import org.json.JSONArray

/**
 * Data class that represents the response from the recipe from ingredients API.
 *
 * @property status The status of the API call
 * @property recipes The recipes that match the ingredients
 * @property returnedCode The code returned by the API (default 200)
 */
data class RecipeFromIngredientsResponseAPI(
    val status: APIResponse,
    val recipes: List<Recipe>,
    val returnedCode: Int = 200
) {
  companion object {
    /** Create a failure response. */
    fun failure(): RecipeFromIngredientsResponseAPI {
      return RecipeFromIngredientsResponseAPI(status = APIResponse.FAILURE, recipes = emptyList())
    }

    /**
     * Parse a JSON array into a RecipeFromIngredientsResponseAPI object.
     *
     * @param jsonArray The JSON array to parse
     */
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
        return failure()
      }
    }
  }
}
