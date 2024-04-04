package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface FirestoreService {
    fun storeMeal(meal: Meal): Task<DocumentReference>
    fun getMeal(mealId: String): Task<Meal?>
    fun getAllMeals(): Task<List<Meal>>
}
