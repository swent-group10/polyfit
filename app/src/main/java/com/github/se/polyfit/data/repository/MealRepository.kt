package com.github.se.polyfit.data.repository

import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealRepository(
    private val mealDao: MealDao,
    private val mealFirebaseRepository: MealFirebaseRepository
) {

    suspend fun storeMeal(meal: Meal) {
        withContext(Dispatchers.IO) {
            mealDao.insert(meal)
            mealFirebaseRepository.storeMeal(meal)
        }
    }

    suspend fun getMeal(mealId: Int): Meal? {
        return withContext(Dispatchers.IO) {
            mealDao.getMeal(mealId) ?: mealFirebaseRepository.getMeal(mealId)
        }
    }

    suspend fun getAllMeals(): List<Meal> {
        return withContext(Dispatchers.IO) {
            val localMeals = mealDao.getAllMeals()
            if (localMeals.isNotEmpty()) {
                localMeals
            } else {
                mealFirebaseRepository.getAllMeals()
            }
        }
    }
}