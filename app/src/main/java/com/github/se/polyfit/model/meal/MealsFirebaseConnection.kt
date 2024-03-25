package com.github.se.polyfit.model.meal

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class MealsFirebaseConnection {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userCollection by lazy { db.collection("users") }


    fun addMeal(uid: String, meal: Meal): Task<Void> {
        val serializedData = serializeMeal(meal)
        return userCollection.document(uid).collection("meals").add(serializedData)
            .addOnFailureListener { e ->
                Log.e("FirebaseConnection", "Error adding document", e)
            }
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "FirebaseConnection",
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )
            }.continueWith {
                null
            }
    }


    // the uid serves as the document id
    fun getMeal(uid: String): Task<Meal> {
        // Implement this function
    }

    suspend fun getAllMeals(): List<Meal> {
        // Implement this function
    }

    fun updateMeal(
        // Add parameters here
    ): Task<Void> {
        // Implement this function
    }

    private fun serializeMeal(data: Meal): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        // Convert each property manually
        map["uid"] = data.uid
        map["occasion"] = data.occasion
        map["name"] = data.name
        map["calories"] = data.calories
        map["protein"] = data.protein
        map["carbohydrates"] = data.carbohydrates
        map["fat"] = data.fat

        return map
    }

    private fun deserializeMeal(data: Map<String, Any>): Meal {
        // Convert each property manually
        val uid = data["uid"] as String
        val occasion = data["occasion"] as MealOccasion
        val name = data["name"] as String
        val calories = data["calories"] as Double
        val protein = data["protein"] as Double
        val carbohydrates = data["carbohydrates"] as Double
        val fat = data["fat"] as Double

        return Meal(
            uid,
            occasion,
            name,
            calories,
            protein,
            carbohydrates,
            fat
        )
    }

    fun removeMeal(uid: String): Task<Void> {
        return collection.document(uid).delete().addOnFailureListener { e ->
            Log.e("FirebaseConnection", "Error deleting document", e)
        }
    }
}