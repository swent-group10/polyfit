package com.github.se.polyfit.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/** Repository for meals responsible for handling the firebase repository and the local database */
class MealRepository(
    private val context: Context,
    private val mealFirebaseRepository: MealFirebaseRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
  private val checkConnecivity = connectivityChecker(context)

  suspend fun storeMeal(meal: Meal) =
      withContext(dispatcher) { mealFirebaseRepository.storeMeal(meal).await() }

  suspend fun getMeal(firebaseID: String) =
      withContext(dispatcher) { mealFirebaseRepository.getMeal(firebaseID).await() }

  suspend fun getAllMeals() =
      withContext(dispatcher) { mealFirebaseRepository.getAllMeals().await() }

  suspend fun deleteMeal(firebaseID: String) =
      withContext(dispatcher) { mealFirebaseRepository.deleteMeal(firebaseID).await() }
}

class connectivityChecker(private val context: Context) {
  fun checkConnection(): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val currentNetwork = connectivityManager.activeNetwork

    return currentNetwork == null
  }
}
