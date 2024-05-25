package com.github.se.polyfit.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.google.firebase.firestore.DocumentReference
import java.time.LocalDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/** Repository for meals responsible for handling the firebase repository and the local database */
class MealRepository(
    private val context: Context,
    private val mealFirebaseRepository: MealFirebaseRepository,
    private val mealDao: MealDao,
    private val user: User,
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
    var returnVal: DocumentReference? = null
    return withContext(dispatcher) {
      try {
        if (checkConnectivity.checkConnection()) {
          if (isDataOutdated) {
            updateFirebase()
            isDataOutdated = false
          }
          returnVal = mealFirebaseRepository.storeMeal(meal).await()
        } else {
          isDataOutdated = true
        }

        mealDao.insert(meal)
        return@withContext returnVal
      } catch (e: Exception) {
        Log.e("MealRepository", "Failed to store meal: ${e.message}")
        throw Exception("Failed to store meal: ${e.message}")
      }
    }
  }

  fun getMealById(mealId: String): Meal? {
    return mealDao.getMealById(mealId)
  }

  fun getAllMeals(): List<Meal> {
    return mealDao.getAllMeals(userId = user.id)
  }

  suspend fun getMealsOnDate(date: LocalDate): List<Meal> {
    return withContext(this.dispatcher) { mealDao.getMealsCreatedOnDate(date, user.id) }
  }

  suspend fun deleteMeal(id: String) {
    if (checkConnectivity.checkConnection()) {
      if (isDataOutdated) {
        updateFirebase()
        isDataOutdated = false
      }
      withContext(dispatcher) {
        try {
          mealFirebaseRepository.deleteMeal(id).await()
        } catch (e: Exception) {
          Log.e("MealRepository", "Failed to delete meal: ${e.message}")
          throw Exception("Failed to delete meal: ${e.message}")
        }
      }
    } else {
      isDataOutdated = true
    }
    withContext(dispatcher) { mealDao.deleteById(id) }
  }

  /** Returns a list of unique ingredients */
  suspend fun getAllIngredients(): List<Ingredient> {
    return mealDao.getAllIngredients(user.id).distinctBy { it.name }
  }

  private suspend fun updateFirebase() {
    withContext(dispatcher) {
      mealDao.getAllMeals(user.id).forEach {
        try {
          mealFirebaseRepository.storeMeal(it)
        } catch (e: Exception) {
          Log.e("MealRepository", "Failed to store meal: ${e.message}")
          throw Exception("Failed to store meal: ${e.message}")
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
