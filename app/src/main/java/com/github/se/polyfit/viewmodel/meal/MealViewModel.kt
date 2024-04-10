package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.launch

class MealViewModel(userID: String, firebaseID: String = "") : ViewModel() {
  // after friday use hilt dependency injection to make code cleaner, for now i guess this is ok
  private val mealRepo: MealRepository = MealRepository(userID)

  private val _meal: MutableLiveData<Meal> = MutableLiveData(null)
  val meal: LiveData<Meal> = _meal

  init {
    if (firebaseID.isNotEmpty()) {
      mealRepo.getMeal(firebaseID).addOnCompleteListener {
        if (it.isSuccessful) {
          _meal.value = it.result
        }
      }
    } else {
      _meal.value =
          Meal(
              MealOccasion.NONE,
              "Temporary Meal Name",
              0,
              20.0,
              NutritionalInformation(mutableListOf()),
              mutableListOf(),
              firebaseID)
    }
  }

  fun getMealFromImage(imageBitmap: Bitmap): LiveData<Meal> {
    // Convert to file
    val file = File.createTempFile("image", ".jpg")
    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
    // Gets Api response

    val meal = MutableLiveData<Meal>()
    meal.value = Meal.default()

    viewModelScope.launch {
      val apiResponse = SpoonacularApiCaller.imageAnalysis(file)
      if (apiResponse.status == APIResponse.SUCCESS) {
        // chooses from a bunch of recipes
        val recipeInformation = SpoonacularApiCaller.getMealNutrition(apiResponse.recipes.first())

        if (recipeInformation.status == APIResponse.SUCCESS) {
          val newMeal =
              Meal(
                  MealOccasion.NONE,
                  apiResponse.category,
                  apiResponse.recipes.first().toLong(),
                  20.0,
                  NutritionalInformation(recipeInformation.nutrients.toMutableList()),
                  recipeInformation.ingredients.toMutableList(),
                  // firebase id not defined yet because no calls to store the information
                  "")

          mealRepo.storeMeal(newMeal).addOnCompleteListener { it ->
            newMeal.firebaseId = it.result.id

            meal.value = newMeal

            // maybe in next spring better to store ingredients in a local database
            _ingredients.value =
                _ingredients.value?.plus(
                    newMeal.ingredients.map {
                      Ingredient(it.name, it.id, 0.0, it.unit, it.nutritionalInformation.deepCopy())
                    })
                    ?: newMeal.ingredients.map {
                      Ingredient(it.name, it.id, 0.0, it.unit, it.nutritionalInformation.deepCopy())
                    }
          }
        }
      }
    }

    return meal
  }

  fun setMeal() {
    if (_meal.value == null) {
      throw IllegalStateException("Meal is null")
    }

    if (!_meal.value!!.isComplete()) {
      throw Exception("Meal is incomplete")
    }

    mealRepo.storeMeal(_meal.value!!)
  }

  fun addIngredient(ingredient: Ingredient) {
    val currentMeal = _meal.value
    if (currentMeal != null) {
      // TODO: Ideally use the Meal.addIngredient method but its not working rn...
      val updatedMeal =
          currentMeal.copy(
              ingredients = currentMeal.ingredients.toMutableList().apply { add(ingredient) },
              nutritionalInformation =
                  currentMeal.nutritionalInformation.plus(ingredient.nutritionalInformation))
      _meal.value = updatedMeal // Emit the new instance as the current state
    }
  }

  fun removeIngredient(ingredient: Ingredient) {
    val currentMeal = _meal.value
    if (currentMeal != null) {
      // TODO: Use a method of Meal somehow
      val updatedMeal =
          currentMeal.copy(
              ingredients = currentMeal.ingredients.toMutableList().apply { remove(ingredient) },
              nutritionalInformation =
                  currentMeal.nutritionalInformation.minus(ingredient.nutritionalInformation))
      _meal.value = updatedMeal
    }
  }
}