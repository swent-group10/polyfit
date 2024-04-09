package com.github.se.polyfit.data.repository

import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Repository for meals responsible for handling the firebase repository and the local database
 * (future implementation)
 */
class MealRepository(
    private val mealFirebaseRepository: MealFirebaseRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    fun storeMeal(meal: Meal) = mealFirebaseRepository.storeMeal(meal)

    fun getMeal(firebaseID: String) =
        mealFirebaseRepository.getMeal(firebaseID)

    fun getAllMeals() =
        mealFirebaseRepository.getAllMeals()

    fun deleteMeal(firebaseID: String) =
        mealFirebaseRepository.deleteMeal(firebaseID)
}
