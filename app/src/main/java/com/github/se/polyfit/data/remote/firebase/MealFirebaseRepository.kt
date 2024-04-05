package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MealFirebaseRepository(
    private val userId: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val mealCollection = db.collection("users").document(userId).collection("meals")

    fun storeMeal(meal: Meal): Task<DocumentReference> {
        val mealData = Meal.serialize(meal)
        return mealCollection.add(mealData)
    }

    // maybe ok for now, but in the future will need to find a better way to
    // distinguish between meals
    fun getMeal(mealId: String): Task<Meal?> {
        return getAllMeals().continueWith { task ->
            if (task.isSuccessful) {

                task.result?.find { it.mealID.toString() == mealId }
            } else {
                throw Exception("Failed to fetch meals : " + task.exception?.message)
            }
        }
    }

    fun getAllMeals(): Task<List<Meal>> {
        return mealCollection.get().continueWith { task ->
            if (task.isSuccessful) {

                task.result?.documents?.mapNotNull { document ->
                    document.data?.let { Meal.deserialize(it) }
                } ?: listOf()
            } else {
                throw Exception("Failed to fetch meals")
            }
        }
    }
}
