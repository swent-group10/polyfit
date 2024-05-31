package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.meal.Meal
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository for storing and fetching meals from the Firebase Firestore database
 *
 * @param userId the id of the user whose meals are being stored/fetched
 * @param db the Firestore database instance to use
 */
class MealFirebaseRepository(
    userId: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

  private val mealCollection = db.collection("users").document(userId).collection("meals")

  /**
   * Stores a meal in the database. If the meal is not already stored in the database, a new
   * document will be created. If the meal is already stored in the database, the existing document
   * will be updated.
   *
   * @param meal the meal to store
   * @return a Task that will resolve to the DocumentReference of the stored meal
   */
  fun storeMeal(meal: Meal): Task<DocumentReference> {
    val mealData = Meal.serialize(meal)

    return mealCollection.document(meal.id).set(mealData).continueWithTask {
      if (it.isSuccessful) {
        Tasks.forResult(mealCollection.document(meal.id))
      } else {
        Log.e("MealFirebaseRepository", "Failed to store meal: ${it.exception?.message}")
        throw Exception("Failed to store meal: ${it.exception?.message}")
      }
    }
  }

  /**
   * Fetches a meal from the database
   *
   * @param id the id of the meal to fetch
   * @return a Task that will resolve to the fetched meal or null if the meal does not exist
   */
  fun getMeal(id: String): Task<Meal?> {
    return mealCollection.document(id).get().continueWith { task ->
      if (task.isSuccessful) {
        task.result?.data?.let {
          try {
            Meal.deserialize(it)
          } catch (e: Exception) {
            Log.e("MealFirebaseRepository", "Error processing meal document", e)
            throw Exception("Error processing meal document : ${e.message} ")
          }
        }
      } else {
        Log.e("MealFirebaseRepository", "Failed to fetch meal: ${task.exception?.message}")
        throw Exception("Failed to fetch meal: ${task.exception?.message}")
      }
    }
  }

  /**
   * Fetches all meals from the database
   *
   * @return a Task that will resolve to a list of all meals
   */
  fun getAllMeals(): Task<List<Meal?>> {
    return mealCollection.get().continueWith { task ->
      if (task.isSuccessful) {
        try {
          // Convert documents to Meal objects
          task.result.documents.mapNotNull { document ->
            document.data?.let { Meal.deserialize(it) }
          }
        } catch (e: Exception) {
          Log.e("MealFirebaseRepository", "Error processing meal documents", e)
          throw Exception("Error processing meal documents: ${e.message}")
        }
      } else {
        Log.e("MealFirebaseRepository", "Failed to fetch meals: ${task.exception?.message}")
        throw Exception("Failed to fetch meals: ${task.exception?.message}")
      }
    }
  }

  /**
   * Deletes a meal from the database
   *
   * @param id the id of the meal to delete
   * @return a Task that will resolve to Unit if the meal was deleted successfully
   */
  fun deleteMeal(id: String): Task<Unit> {
    return mealCollection.document(id).delete().continueWithTask {
      if (it.isSuccessful) {
        Tasks.forResult(Unit)
      } else {
        Log.e("MealFirebaseRepository", "Failed to delete meal: ${it.exception?.message}")
        throw Exception("Failed to delete meal: ${it.exception?.message}")
      }
    }
  }
}
