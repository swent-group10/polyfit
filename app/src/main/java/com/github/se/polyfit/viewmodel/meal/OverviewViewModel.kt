package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel
@Inject
constructor(private val mealDao: MealDao, private val spoonacularApiCaller: SpoonacularApiCaller) :
    ViewModel() {

  fun storeMeal(imageBitmap: Bitmap?): Long? {
    if (imageBitmap == null) {
      Log.e("OverviewViewModel", "Image is null")
      return null
    } else {
      val meal = spoonacularApiCaller.getMealsFromImage(imageBitmap)

      val mealId = mealDao.insert(meal)

      if (mealId == null) {
        Log.e("OverviewViewModel", "Meal could not be inserted into the database")
        return null
      } else {
        return mealId
      }
    }
  }

  fun deleteByDBId(mealDatabaseId: Long) {
    mealDao.deleteByDatabaseID(mealDatabaseId)
  }

  fun getMealsByCalorieRange(minCalories: Double, maxCalories: Double): List<Meal> {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals()

    // Filter meals based on their calorie content
    val filteredMeals =
        allMeals.filter { meal ->
          val calories =
              meal.nutritionalInformation.nutrients.find { it.nutrientType == "calories" }?.amount
          calories != null && calories >= minCalories && calories <= maxCalories
        }

    // Log the result
    if (filteredMeals.isEmpty()) {
      Log.e("OverviewViewModel", "No meals found in the specified calorie range")
    } else {
      Log.i("OverviewViewModel", "Found ${filteredMeals.size} meals in the specified calorie range")
    }

    // Return the filtered meals
    return filteredMeals
  }

  fun getMealsByName(name: String): List<Meal> {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals()

    // Filter meals based on their name
    val filteredMeals = allMeals.filter { meal -> meal.name.contains(name, ignoreCase = true) }

    // Log the result
    if (filteredMeals.isEmpty()) {
      Log.e("OverviewViewModel", "No meals found with the specified name")
    } else {
      Log.i("OverviewViewModel", "Found ${filteredMeals.size} meals with the specified name")
    }

    // Return the filtered meals
    return filteredMeals
  }

  fun getMealsByOccasion(occasion: MealOccasion): List<Meal> {
    // Get all meals from the database
    val allMeals = mealDao.getAllMeals()

    // Filter meals based on their occasion
    val filteredMeals = allMeals.filter { meal -> meal.occasion == occasion }

    // Log the result
    if (filteredMeals.isEmpty()) {
      Log.e("OverviewViewModel", "No meals found for the specified occasion")
    } else {
      Log.i("OverviewViewModel", "Found ${filteredMeals.size} meals for the specified occasion")
    }

    // Return the filtered meals
    return filteredMeals
  }
}
