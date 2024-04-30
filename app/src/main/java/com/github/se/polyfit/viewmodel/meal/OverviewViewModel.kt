package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(private val mealDao: MealDao) : ViewModel() {
    private val spoonacularApiCaller = SpoonacularApiCaller()

    fun storeMeal(imageBitmap: Bitmap?): Long? {
        if (imageBitmap == null) return null

        val meal = spoonacularApiCaller.getMealsFromImage(imageBitmap)
            .observeForever {
                var mealId: Long? = null
                viewModelScope.launch(Dispatchers.IO) {
                    mealId = mealDao.insert(it)


//                mealViewModel.setMealData(it)
//            mealViewModel.storeMeal()
//            navController.navigate(Route.AddMeal)

                }

            }
        return null

    }
}