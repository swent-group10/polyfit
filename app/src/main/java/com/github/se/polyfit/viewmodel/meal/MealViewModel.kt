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

class MealViewModel(userID: String) : ViewModel() {
  // after friday use hilt dependency injection to make code cleaner, for now
  // i guess this is ok
  private val mealRepo: MealRepository = MealRepository(userID)
  private val _ingredients: MutableLiveData<List<Ingredient>> = MutableLiveData(listOf())

  fun getAllMeals(): LiveData<List<Meal?>> {
    var liveMeals = MutableLiveData<List<Meal?>>()
    mealRepo.getAllMeals().continueWith {
      if (it.isSuccessful) {
        liveMeals.value = it.result
      } else {
        liveMeals.value = listOf(Meal.default())
      }
    }

    // way to avoid crashing if getting all the meals is not successfull
    // also can livedata can be observed to make the view non blocking
    return liveMeals
  }

  fun getMealsFromImage(imageBitmap: Bitmap): LiveData<Meal> {
    // need to conver to File
    var file = File.createTempFile("image", ".jpg")
    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
    // Gets Api response

    val meal = MutableLiveData<Meal>()
    meal.value = Meal.default()

    viewModelScope.launch {
      val apiReponse = SpoonacularApiCaller.imageAnalysis(file)
      if (apiReponse.status == APIResponse.SUCCESS) {
        // chooses from a bunch of recipes
        val recipeInformation = SpoonacularApiCaller.getMealNutrition(apiReponse.recipes.first())

        if (recipeInformation.status == APIResponse.SUCCESS) {
          val newMeal =
              Meal(
                  MealOccasion.NONE,
                  apiReponse.category,
                  apiReponse.recipes.first().toLong(),
                  20.0,
                  NutritionalInformation(recipeInformation.nutrients.toMutableList()),
                  recipeInformation.ingredients.toMutableList(),
                  // firebase id not defined yet because no calls to store the information
                  "")

          mealRepo.storeMeal(newMeal).addOnCompleteListener {
            newMeal.firebaseId = it.result.id

            meal.value = newMeal

            // maybe in next spring better to store ingredients in a local database
            newMeal.ingredients.map {
              Ingredient(it.name, it.id, 0.0, it.unit, it.nutritionalInformation.deepCopy())
            }
          }
        }
      }
    }

    return meal
  }

  fun setMeal(meal: Meal) {
    mealRepo.storeMeal(meal)
  }

  fun getAllIngredients(): LiveData<List<Ingredient>> {
    return _ingredients
  }
}

/*
features requested by mesha :


getIngredients (for initial display, i guess either a list of all ingredients or if we support it, one for confirmed and one for potential ingredients)
addIngredient (add from potential ingredient list, or from the add button)
removeIngredient (for when expanding the ingredient is supported)



 */
