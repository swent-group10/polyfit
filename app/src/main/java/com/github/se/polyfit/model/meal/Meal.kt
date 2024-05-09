package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate
import java.util.UUID

// modeled after the log meal api
data class Meal(
    var occasion: MealOccasion,
    var name: String,
    val id: String = UUID.randomUUID().toString(),
    val mealTemp: Double = 20.0,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    var createdAt: LocalDate = LocalDate.now(),
    val tags: MutableList<MealTag> = mutableListOf()
) {
  var nutritionalInformation: NutritionalInformation = NutritionalInformation()

  init {
    updateMeal()
  }

  fun deepCopy(
      occasion: MealOccasion = this.occasion,
      name: String = this.name,
      id: String = this.id,
      mealTemp: Double = this.mealTemp,
      ingredients: MutableList<Ingredient> = this.ingredients,
      createdAt: LocalDate = this.createdAt,
      tags: MutableList<MealTag> = this.tags
  ): Meal {
    val newIngredients = ingredients.map { it.deepCopy() }.toMutableList()
    val newTags = tags.map { it.copy() }.toMutableList()

    return Meal(
        occasion = occasion,
        name = name,
        id = id,
        mealTemp = mealTemp,
        ingredients = newIngredients,
        createdAt = createdAt,
        tags = newTags)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Meal) return false

    if (id != other.id) return false
    if (name != other.name) return false
    if (occasion != other.occasion) return false
    if (mealTemp != other.mealTemp) return false
    if (nutritionalInformation != other.nutritionalInformation) return false
    if (ingredients != other.ingredients) return false
    if (createdAt != other.createdAt) return false
    if (tags != other.tags) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + occasion.hashCode()
    result = 31 * result + mealTemp.hashCode()
    result = 31 * result + nutritionalInformation.hashCode()
    result = 31 * result + ingredients.hashCode()
    result = 31 * result + createdAt.hashCode()
    result = 31 * result + tags.hashCode()

    return result
  }

  fun calculateTotalNutrient(nutrientType: String): Double {
    return nutritionalInformation.calculateTotalNutrient(nutrientType)
  }

  fun calculateTotalCalories(): Double {
    return calculateTotalNutrient("calories")
  }

  fun addIngredient(ingredient: Ingredient) {
    ingredients.add(ingredient)
    updateMeal()
  }

  fun isComplete(): Boolean {
    return name.isNotEmpty() &&
        ingredients.isNotEmpty() &&
        nutritionalInformation.nutrients.isNotEmpty()
  }

  fun getNutrient(nutrientType: String): Nutrient? {
    return this.nutritionalInformation.getNutrient(nutrientType)
  }

  fun getMacros(): Map<String, Double> {
    val macros = mutableMapOf<String, Double>()
    val protein = getNutrient("protein")?.amount ?: 0.0
    val fat = getNutrient("fat")?.amount ?: 0.0
    val carbs = getNutrient("carbohydrates")?.amount ?: 0.0
    macros["protein"] = protein
    macros["fat"] = fat
    macros["carbohydrates"] = carbs
    return macros
  }

  private fun updateMeal() {
    var newNutritionalInformation = NutritionalInformation()

    for (ingredient in ingredients) {
      newNutritionalInformation += ingredient.nutritionalInformation
    }

    nutritionalInformation = newNutritionalInformation
  }

  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

  companion object {

    fun serialize(data: Meal): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["id"] = data.id
        this["occasion"] = data.occasion.name
        this["name"] = data.name
        this["mealTemp"] = data.mealTemp
        this["ingredients"] = data.ingredients.map { Ingredient.serialize(it) }
        this["createdAt"] = data.createdAt.toString()
        this["tags"] = data.tags.map { MealTag.serialize(it) }
      }
    }

    fun deserialize(data: Map<String, Any>): Meal {
      return try {
        val id = data["id"] as String
        val occasion = data["occasion"].let { MealOccasion.valueOf(it as String) }
        val mealTemp = data["mealTemp"] as Double
        val name = data["name"] as String
        val createdAt = LocalDate.parse(data["createdAt"] as String)
        val tags =
            (data["tags"] as List<Map<String, Any>>).map { MealTag.deserialize(it) }.toMutableList()

        val newMeal =
            Meal(
                occasion = occasion,
                name = name,
                id = id,
                mealTemp = mealTemp,
                createdAt = createdAt,
                tags = tags)

        val ingredientsDatas = data["ingredients"] as? List<Map<String, Any>>
        if (ingredientsDatas != null) {
          for (ingredientData in ingredientsDatas) {
            newMeal.addIngredient(Ingredient.deserialize(ingredientData))
          }
        }
        newMeal
      } catch (e: Exception) {
        Log.e("Meal", "Failed to deserialize Meal object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Meal object", e)
      }
    }

    fun default(): Meal {
      return Meal(
          occasion = MealOccasion.OTHER,
          name = "",
          id = UUID.randomUUID().toString(),
          mealTemp = 20.0,
          ingredients = mutableListOf(),
          createdAt = LocalDate.now(),
          tags = mutableListOf())
    }
  }
}
