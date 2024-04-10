package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.meal.Meal
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MealFirebaseRepository(
    userId: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val mealCollection = db.collection("users").document(userId).collection("meals")

    /**
     * Stores a meal in the database. If the meal is not already stoed in the database, a new
     * document will be created. If the meal is already stored in the database, the existing
     * document will be updated.
     *
     * @param meal the meal to store
     * @return a Task that will resolve to the DocumentReference of the stored meal
     */
    fun storeMeal(meal: Meal): Task<DocumentReference> {
        val mealData = Meal.serialize(meal)

        return if (meal.firebaseId.isNotEmpty()) {
            mealCollection.document(meal.firebaseId).set(mealData).continueWithTask {
                if (it.isSuccessful) {
                    Tasks.forResult(mealCollection.document(meal.firebaseId))
                } else {
                    Log.e(
                        "MealFirebaseRepository",
                        "Failed to store meal: ${it.exception?.message}"
                    )
                    throw Exception("Failed to store meal: ${it.exception?.message}")
                }
            }
        } else {
            mealCollection.add(mealData).continueWithTask { task ->
                if (task.isSuccessful) {
                    meal.firebaseId = task.result!!.id
                    Tasks.forResult(mealCollection.document(meal.firebaseId))
                } else {
                    Log.e(
                        "MealFirebaseRepository",
                        "Failed to store meal: ${task.exception?.message}"
                    )
                    throw Exception("Failed to store meal: ${task.exception?.message}")
                }
            }
        }
    }

    /**
     * Fetches a meal from the database
     *
     * @param firebaseID the id of the meal to fetch
     * @return a Task that will resolve to the fetched meal or null if the meal does not exist
     */
    fun getMeal(firebaseID: String): Task<Meal?> {
        return mealCollection.document(firebaseID).get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.data?.let {
                    try {
                        Meal.deserialize(it).apply { this.firebaseId = firebaseID }
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
                        document.data?.let {
                            Meal.deserialize(it).apply { this.firebaseId = document.id }
                        }
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
     * @param firebaseID the id of the meal to delete
     * @return a Task that will resolve to Unit if the meal was deleted successfully
     */
    fun deleteMeal(firebaseID: String): Task<Unit> {
        return mealCollection.document(firebaseID).delete().continueWithTask {
            if (it.isSuccessful) {
                Tasks.forResult(Unit)
            } else {
                Log.e("MealFirebaseRepository", "Failed to delete meal: ${it.exception?.message}")
                throw Exception("Failed to delete meal: ${it.exception?.message}")
            }
        }
    }
}
