package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

// modeled after the log meal api
data class Meal(
    val occasion: MealOccasion,
    val name: String,
    val mealID: Long,
    // represent the ideal temperature at which should be eaten at,
    // usefull for later features
    val mealTemp: Double = 20.0,
    val nutritionalInformation: NutritionalInformation,
    val ingredients: MutableList<Ingredient> = mutableListOf(),
    var firebaseId: String = ""
) {
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
    return result
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

  private fun updateMeal() {

    var newNutritionalInformation = NutritionalInformation(mutableListOf())

    for (ingredient in ingredients) {
      newNutritionalInformation += ingredient.nutritionalInformation
    }
    nutritionalInformation.update(newNutritionalInformation)
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
        this["nutritionalInformation"] =
            NutritionalInformation.serialize(data.nutritionalInformation)
        this["ingredients"] = data.ingredients.map { Ingredient.serialize(it) }
      }
    }

    fun deserialize(data: Map<String, Any>): Meal {
      return try {
        val mealID = data["mealID"] as Long
        val occasion = data["occasion"].let { MealOccasion.valueOf(it as String) }

        val mealTemp = data["mealTemp"] as Double

        val name = data["name"] as String

        val nutritionalInformationData =
            NutritionalInformation.deserialize(
                data["nutritionalInformation"] as List<Map<String, Any>>)

        val newMeal = Meal(occasion, name, mealID, mealTemp, nutritionalInformationData)

        val ingredientsData = data["ingredients"] as? List<Map<String, Any>>
        if (ingredientsData != null) {
          for (ingredientData in ingredientsData) {
            newMeal.addIngredient(Ingredient.deserialize(ingredientData))
          }
        }
        newMeal
      } catch (e: Exception) {
        Log.e("Meal", "Failed to deserialize Meal object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Meal object", e)
      }
    }
  }
}
