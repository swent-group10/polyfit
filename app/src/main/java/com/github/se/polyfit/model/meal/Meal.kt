package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate

// modeled after the log meal api
data class Meal(
    var occasion: MealOccasion,
    var name: String,
    val mealID: Long,
    // represent the ideal temperature at which should be eaten at,
    // usefull for later features
    val mealTemp: Double = 20.0,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    var firebaseId: String = "",
    var createdAt: LocalDate = LocalDate.now(),
    val tags: MutableList<MealTag> = mutableListOf()
) {
  var nutritionalInformation: NutritionalInformation = NutritionalInformation()
    internal set

  init {
    require(mealID >= 0)

    updateMeal()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Meal) return false

    if (mealID != other.mealID) return false
    if (name != other.name) return false
    if (occasion != other.occasion) return false
    if (mealTemp != other.mealTemp) return false
    if (nutritionalInformation != other.nutritionalInformation) return false
    if (ingredients != other.ingredients) return false
    if (firebaseId != other.firebaseId) return false
    if (createdAt != other.createdAt) return false
    if (tags != other.tags) return false

    return true
  }

  override fun hashCode(): Int {
    var result = mealID.toInt()
    result = 31 * result + name.hashCode()
    result = 31 * result + occasion.hashCode()
    result = 31 * result + mealTemp.hashCode()
    result = 31 * result + nutritionalInformation.hashCode()
    result = 31 * result + ingredients.hashCode()
    result = 31 * result + firebaseId.hashCode()
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
        this["mealID"] = data.mealID
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
        val mealID = data["mealID"] as Long
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
                mealID = mealID,
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
          mealID = 0,
          mealTemp = 20.0,
          ingredients = mutableListOf(),
          firebaseId = "",
          createdAt = LocalDate.now(),
          tags = mutableListOf())
    }
  }
}
