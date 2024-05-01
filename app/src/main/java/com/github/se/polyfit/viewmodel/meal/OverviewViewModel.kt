package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
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
      if (meal == null) {
        Log.e("OverviewViewModel", "Meal could not be created from image")
        return null
      } else {
        val mealId = mealDao.insert(meal)

        if (mealId == null) {
          Log.e("OverviewViewModel", "Meal could not be inserted into the database")
          return null
        } else {
          return mealId
        }
      }
    }
  }

  fun deleteByDBId(mealDatabaseId: Long) {
    mealDao.deleteByDatabaseID(mealDatabaseId)
  }
}
