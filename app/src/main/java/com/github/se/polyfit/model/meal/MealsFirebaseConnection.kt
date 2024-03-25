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
        val task = userCollection.document(uid).collection("meals").get()
        val result = task.continueWith { task ->
            if (task.isSuccessful) {
                val meals = task.result?.documents?.map { deserializeMeal(it.data!!) }
                meals ?: emptyList<Meal>()
            } else {
                emptyList<Meal>()
            }
        }
        return result
    }

    fun updateMeal(
        uid: String,
        meal: Meal
    ): Task<Void> {
        // Implement this function
        return userCollection.document(uid).collection("meals").document(meal.uid)
            .set(serializeMeal(meal))
    }


    fun removeMeal(uid: String): Task<Void> {
        return userCollection.document(uid).delete().addOnFailureListener { e ->
            Log.e("FirebaseConnection", "Error deleting document", e)
        }
    }
}