package com.github.se.polyfit.data.repository

import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Repository for meals responsible for handling the firebase repository and the local database
 * (future implementation)
 */
class MealRepository(
    private val mealFirebaseRepository: MealFirebaseRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

  suspend fun storeMeal(meal: Meal) =
      withContext(dispatcher) { mealFirebaseRepository.storeMeal(meal).await() }

  suspend fun getMeal(firebaseID: String) =
      withContext(dispatcher) { mealFirebaseRepository.getMeal(firebaseID).await() }

  suspend fun getAllMeals() =
      withContext(dispatcher) { mealFirebaseRepository.getAllMeals().await() }

  suspend fun deleteMeal(firebaseID: String) =
      withContext(dispatcher) { mealFirebaseRepository.deleteMeal(firebaseID).await() }
}
