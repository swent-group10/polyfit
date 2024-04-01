package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

// modeled after the log meal api
data class Meal(
    val occasion: MealOccasion,
    val name: String,
    // represent the ideal temperature at which should be eaten at,
    // usefull for later features
    var mealTemp: Double = 20.0,
    val nutritionalInformation: NutritionalInformation,
    private val ingredients: MutableList<Ingredient> = mutableListOf()
) {
  private var uid: String = ""

  /**
   * Updates the uid of the meal. The uid is of the associated user. It can only be set once for
   * consistency.
   *
   * @param newUid The new uid to set.
   */
  fun updateUid(newUid: String) {
    if (uid != "") {
      Log.e(
          "Meal", "Attempted to update the uid of a Meal object that already has a uid. Ignoring.")
    } else {
      uid = newUid
    }
  }

  fun getUid(): String {
    return uid
  }

  fun addIngredient(ingredient: Ingredient) {
    ingredients.add(ingredient)
    updateMeal()
  }

  fun updateMeal() {}

  companion object {

    fun serialize(data: Meal): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["uid"] = data.uid
        this["occasion"] = data.occasion.name
        this["name"] = data.name
        this["mealTemp"] = data.mealTemp
        this["nutritionalInformation"] =
            NutritionalInformation.serialize(data.nutritionalInformation)
      }
    }

    fun deserialize(data: Map<String, Any>): Meal? {
      val uid = data["uid"] as? String
      val occasion = data["occasion"]?.let { MealOccasion.valueOf(it as String) }
      val mealTemp = data["mealTemp"] as? Double
      val name = data["name"] as? String
      val nutritionalInformationData =
          data["nutritionalInformation"] as? Map<String, Map<String, Any>>

      return if (uid != null &&
          occasion != null &&
          mealTemp != null &&
          name != null &&
          nutritionalInformationData != null) {
        val nutritionalInformation = NutritionalInformation.deserialize(nutritionalInformationData)
        Meal(occasion, name, mealTemp, nutritionalInformation).apply { updateUid(uid) }
      } else {
        Log.e("Meal", "Failed to deserialize Meal object")
        null
      }
    }
  }
}
