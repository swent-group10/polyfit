package com.github.se.polyfit.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/** Repository for meals responsible for handling the firebase repository and the local database */
class MealRepository(
    private val context: Context,
    private val mealFirebaseRepository: MealFirebaseRepository,
    private val mealDao: MealDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val checkConnectivity: ConnectivityChecker = ConnectivityChecker(context)
) {
  private var isDataOutdated = false

  /**
   * Stores a meal in the firebase and the local database. Makes sure both database are in sync
   *
   * @param meal the meal to store
   * @return the DocumentReference of the stored meal returns null if not connected to internet
   */
  suspend fun storeMeal(meal: Meal): DocumentReference? {
    Log.d("MealRepository", "Storing meal: $meal")
    return withContext(dispatcher) {
      try {

        if (checkConnectivity.checkConnection()) {
          if (isDataOutdated) {
            updateFirebase()
            isDataOutdated = false
            mealDao.insert(meal)
            return@withContext mealFirebaseRepository.storeMeal(meal).await()
          } else {

            val documentReference = mealFirebaseRepository.storeMeal(meal).await()
            mealDao.insert(meal)
            return@withContext documentReference
          }
        } else {
          isDataOutdated = true
          mealDao.insert(meal)
          return@withContext null
        }
      } catch (e: Exception) {
        Log.e("MealRepository", "Failed to store meal: ${e.message}")
        throw Exception("Failed to store meal: ${e.message}")
      }
    }
  }

  suspend fun getMeal(firebaseID: String): Meal? {
    return mealDao.getMealByFirebaseID(firebaseID)
  }

  suspend fun getAllMeals(): List<Meal> {
    return mealDao.getAllMeals()
  }

  suspend fun deleteMeal(firebaseID: String) {
    if (checkConnectivity.checkConnection()) {
      if (isDataOutdated) {
        updateFirebase()
        isDataOutdated = false
      }
      withContext(dispatcher) {
        try {
          mealFirebaseRepository.deleteMeal(firebaseID).await()
        } catch (e: Exception) {
          Log.e("MealRepository", "Failed to delete meal: ${e.message}")
          throw Exception("Failed to delete meal: ${e.message}")
        }
      }
    } else {
      isDataOutdated = true
    }
    mealDao.deleteByFirebaseID(firebaseID)
  }

  /** Returns a list of unique ingredients */
  suspend fun getAllIngredients(): List<Ingredient> {
    return mealDao.getAllIngredients().distinctBy { it.name }
  }

  private suspend fun updateFirebase() {
    withContext(dispatcher) {
      mealDao.getAllMeals().forEach {
        if (it != null) {
          try {
            mealFirebaseRepository.storeMeal(it)
          } catch (e: Exception) {
            Log.e("MealRepository", "Failed to store meal: ${e.message}")
            throw Exception("Failed to store meal: ${e.message}")
          }
        }
      }
    }
  }

  class ConnectivityChecker(private val context: Context) {
    fun checkConnection(): Boolean {
      val connectivityManager =
          context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val currentNetwork = connectivityManager.activeNetwork

      Log.d("ConnectivityChecker", "Current network: $currentNetwork")
      return currentNetwork != null
    }
  }
}
