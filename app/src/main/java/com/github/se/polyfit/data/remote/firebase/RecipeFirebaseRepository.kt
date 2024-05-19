package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.recipe.Recipe
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class RecipeFirebaseRepository(
    userId: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
  private val recipeCollection = db.collection("users").document(userId).collection("recipes")

  fun storeRecipe(recipe: Recipe): Task<Unit> {
    val documentReference =
        if (recipe.firebaseId.isEmpty()) {
          recipeCollection.document()
        } else {
          recipeCollection.document(recipe.firebaseId)
        }

    return documentReference.set(recipe).continueWith { task ->
      if (task.isSuccessful) {
        if (recipe.firebaseId.isEmpty()) {
          recipe.firebaseId = documentReference.id
        }
      } else {
        val exception = task.exception
        Log.e("RecipeFirebaseRepository", "Error adding recipe: $exception")
        throw Exception("Error adding recipe: $exception")
      }
    }
  }

  fun getRecipe(firebaseId: String): Task<Recipe> {
    if (firebaseId.isEmpty()) {
      Log.e("RecipeFirebaseRepository", "Cannot fetch recipe. No FirebaseId")
      throw IllegalArgumentException("Error. Recipe has no firebase id")
    }

    return recipeCollection.document(firebaseId).get().continueWith { task ->
      if (task.isSuccessful) {
        task.result.data?.let { Recipe.deserialize(it) }
      } else {
        Log.e("RecipeFirebaseRepository", "Error fetching recipe: ${task.exception}")
        throw Exception("Error fetching recipe: ${task.exception}")
      }
    }
  }
}
