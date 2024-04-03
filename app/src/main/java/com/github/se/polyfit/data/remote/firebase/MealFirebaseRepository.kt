package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MealFirebaseRepository(private val userId: String) {

    private val db = FirebaseFirestore.getInstance()
    private val mealCollection = db.collection("users").document(userId).collection("meals")

    suspend fun storeMeal(meal: Meal) {
        val mealData = Meal.serialize(meal)
        mealCollection.add(mealData).await()
    }

    suspend fun getMeal(mealId: Int): Meal? {
        val snapshot = mealCollection.document(mealId.toString()).get().await()
        return snapshot.toObject(Meal::class.java)
    }

    suspend fun getAllMeals(): List<Meal> {
        val snapshots = mealCollection.get().await()
        return snapshots.documents.mapNotNull { it.toObject(Meal::class.java) }
    }
}