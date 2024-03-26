package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.meal.Meal.Companion.deserializeMeal
import com.github.se.polyfit.model.meal.Meal.Companion.serializeMeal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class MealsFirebaseConnection(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    private val userCollection by lazy { db.collection("users") }


    fun addMeal(uid: String, meal: Meal): Task<Void> {
        val serializedData = serializeMeal(meal)
        return userCollection.document(uid).collection("meals").add(serializedData)
            .addOnFailureListener { e ->
                Log.e("FirebaseConnection", "Error adding document", e)
            }
            .addOnSuccessListener { documentReference ->
                Log.e(
                    "FirebaseConnection",
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )
            }.continueWith {
                null
            }
    }

    fun getAllMeals(uid: String): Task<List<Meal>> {
        return userCollection.document(uid).collection("meals").get().continueWith { task ->
            task.result?.documents?.mapNotNull { deserializeMeal(it.data!!) } ?: emptyList()
        }

    }

    fun updateMeal(
        uid: String,
        meal: Meal
    ): Task<Void> {
        val mealMap = serializeMeal(meal)
        return userCollection.document(uid).collection("meals").document(meal.uid).set(mealMap)

    }


}