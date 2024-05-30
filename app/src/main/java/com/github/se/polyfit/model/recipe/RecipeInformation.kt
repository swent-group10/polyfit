package com.github.se.polyfit.model.recipe

import android.util.Log
import com.github.se.polyfit.data.api.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient

/** This is a separate class from the Recipe.kt class to reflect the structure of the api */
data class RecipeInformation(
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    var ingredients: List<Ingredient>,
    var instructions: List<String>,
    val calories: Nutrient = Nutrient("calories", 0.0, MeasurementUnit.KCAL),
    val fat: Nutrient = Nutrient("fat", 0.0, MeasurementUnit.G),
    val protein: Nutrient = Nutrient("protein", 0.0, MeasurementUnit.G),
    val carbs: Nutrient = Nutrient("carbs", 0.0, MeasurementUnit.G),
) {
  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

  companion object {
    fun serialize(recipeInfo: RecipeInformation): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["vegetarian"] = recipeInfo.vegetarian
        this["vegan"] = recipeInfo.vegan
        this["glutenFree"] = recipeInfo.glutenFree
        this["dairyFree"] = recipeInfo.dairyFree
        this["listIngredients"] = recipeInfo.ingredients.map { Ingredient.serialize(it) }
        this["instructions"] = recipeInfo.instructions
      }
    }

    fun deserialize(data: Map<String, Any>): RecipeInformation {
      return try {
        val vegetarian = data["vegetarian"] as Boolean
        val vegan = data["vegan"] as Boolean
        val glutenFree = data["glutenFree"] as Boolean
        val dairyFree = data["dairyFree"] as Boolean
        val listIngredients =
            (data["listIngredients"] as List<Map<String, Any>>).map { Ingredient.deserialize(it) }
        val instructions = data["instructions"] as List<String>

        RecipeInformation(vegetarian, vegan, glutenFree, dairyFree, listIngredients, instructions)
      } catch (e: Exception) {
        Log.e(
            "RecipeInformation", "Failed to deserialize RecipeInformation object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize RecipeInformation object", e)
      }
    }

    fun default(): RecipeInformation {
      // Define the default values for a basic recipe
      val ingredients =
          listOf(
              Ingredient(1, "beef tenderloin", "beef tenderloin", "beef_tenderloin.jpg"),
              Ingredient(2, "olive oil", "olive oil", "olive_oil.jpg"),
              Ingredient(3, "kosher salt", "kosher salt", "kosher_salt.jpg"),
          )

      return RecipeInformation(
          vegetarian = false,
          vegan = false,
          glutenFree = true,
          dairyFree = true,
          ingredients = ingredients,
          instructions =
              listOf(
                  "PreparationFor spice rub: Combine all ingredients in small bowl.",
                  "Do ahead: Can be made 2 days ahead. Store airtight at room temperature. For chimichurri sauce: Combine first 8 ingredients in blender;",
                  " blend until almost smooth. Add 1/4 of parsley,",
                  "1/4 of cilantro, and 1/4 of mint; blend until incorporated. Add remaining herbs in 3 more additions, pureeing until almost smooth after each addition.",
                  " Do ahead: Can be made 3 hours ahead. Cover; chill. For beef tenderloin:",
                  "Let beef stand at room temperature 1 hour. Prepare barbecue (high heat). Pat beef dry with paper towels; brush with oil. Sprinkle all over with spice rub,",
                  "using all of mixture (coating will be thick). Place beef on grill; sear 2 minutes on each side. Reduce heat to medium-high.",
                  "Grill uncovered until instant-read thermometer inserted into thickest part of beef registers 130F for medium-rare, moving beef to cooler part of grill as needed to prevent burning, and turning occasionally, about 40 minutes.",
                  "Transfer to platter; cover loosely with foil and let rest 15 minutes. Thinly slice beef crosswise. Serve with chimichurri sauce. *Available at specialty foods stores and from tienda.com."))
    }
  }
}
