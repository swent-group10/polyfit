package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.meal.Meal.Companion.serializeMeal
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore

class MealsRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

  private val userCollection by lazy { db.collection("users") }

  fun addMeal(uid: String, meal: Meal): Task<Void> {
    val newMealDoc = userCollection.document(uid).collection("meals").document()
    meal.updateUid(newMealDoc.id)
    val serializedData = serializeMeal(meal)
    val addMealTask =
        newMealDoc
            .set(serializedData.first)
            .addOnFailureListener { e -> Log.e("FirebaseConnection", "Error adding document", e) }
            .addOnSuccessListener {
              Log.e("FirebaseConnection", "DocumentSnapshot added with ID: ${newMealDoc.id}")
            }

    serializedData.second.forEach { ingredient ->
      addMealTask
          .continueWithTask { newMealDoc.collection("ingredients").add(ingredient) }
          .addOnFailureListener({ e -> Log.e("FirebaseConnection", "Error adding ingredient", e) })
    }

    return addMealTask.continueWith { null }
  }

  fun getAllMeals(uid: String): Task<List<Meal>> {
    val taskSource = TaskCompletionSource<List<Meal>>()

    userCollection
        .document(uid)
        .collection("meals")
        .get()
        .addOnSuccessListener { documents ->
          val mealTasks = ArrayList<Task<Meal>>()

          documents.forEach { document ->
            val mealTask =
                document.reference.collection("ingredients").get().continueWithTask { ingredientTask
                  ->
                  val ingredients =
                      ingredientTask.result
                          ?.mapNotNull { ingredientDoc -> ingredientDoc.data }
                          .orEmpty()

                  val data = document.data
                  if (data != null) {
                    Tasks.forResult(Meal.deserializeMeal(data, ingredients))
                  } else {
                    Tasks.forException(NullPointerException("Document data is null"))
                  }
                }
            mealTask.let { mealTasks.add(it) }
          }

          Tasks.whenAllSuccess<Meal>(mealTasks)
              .addOnSuccessListener { meals -> taskSource.setResult(meals) }
              .addOnFailureListener { exception -> taskSource.setException(exception) }
        }
        .addOnFailureListener { exception -> taskSource.setException(exception) }

    return taskSource.task
  }

  fun updateMeal(uid: String, meal: Meal): Task<Void> {
    if (meal.getUid().isNullOrEmpty()) {
      Log.d("FirebaseConnection", "Meal uid is null")
      return addMeal(uid, meal)
    }
    userCollection
        .document(uid)
        .collection(("meals"))
        .document(meal.getUid())
        .set(serializeMeal(meal).first)

    val mealMap = serializeMeal(meal)
    return userCollection.document(uid).collection("meals").document(meal.getUid()).set(mealMap)
  }
}
