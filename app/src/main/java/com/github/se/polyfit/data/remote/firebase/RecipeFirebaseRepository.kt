package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.recipe.Recipe
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository class for handling all Firebase operations related to recipes. This class is
 * responsible for storing and fetching recipes in the Firestore database.
 *
 * @param userId The user ID to use for storing and fetching recipes.
 * @param db The Firestore database instance to use for storing and fetching recipes.
 */
class RecipeFirebaseRepository(
    userId: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
  private val recipeCollection = db.collection("users").document(userId).collection("recipes")

  /**
   * Stores a recipe in the Firestore database. If the recipe has no Firebase ID, a new document is
   * created. If the recipe already has a Firebase ID, the existing document is updated.
   *
   * @param recipe The recipe to store.
   * @return A Task that completes when the recipe has been stored.
   * @throws Exception If an error occurs while storing the recipe.
   */
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

  /**
   * Fetches a recipe from the Firestore database.
   *
   * @param firebaseId The Firebase ID of the recipe to fetch.
   * @return A Task that completes with the fetched recipe.
   * @throws IllegalArgumentException If the recipe has no Firebase ID.
   * @throws Exception If an error occurs while fetching the recipe.
   */
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
