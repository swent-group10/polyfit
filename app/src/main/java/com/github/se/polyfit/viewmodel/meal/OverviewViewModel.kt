package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(private val mealDao: MealDao) : ViewModel() {
    private val spoonacularApiCaller = SpoonacularApiCaller()

    suspend fun storeMeal(imageBitmap: Bitmap?): Long? {
        return if (imageBitmap == null) {
            Log.e("OverviewViewModel", "Image is null")
            null
        } else {
            val meal = spoonacularApiCaller.getMealsFromImage(imageBitmap)
            mealDao.insert(meal)

        }

    }
}