package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.data.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository class for handling all Firebase operations related to users. This class is responsible
 * for storing and fetching users in the Firestore database.
 *
 * @param db The Firestore database instance to use for storing and fetching users.
 */
class UserFirebaseRepository(db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
  private val userCollection = db.collection("users")

  /**
   * Fetches a user from the Firestore database.
   *
   * @param userId The ID of the user to fetch.
   * @return A Task that completes with the fetched user.
   * @throws Exception If an error occurs while fetching the user.
   */
  fun getUser(userId: String): Task<User?> {
    return userCollection.document(userId).get().continueWith { task ->
      if (task.isSuccessful) {
        task.result.data?.let { User.deserialize(it) }
      } else {
        Log.e("UserFirebaseRepository", "Error fetching user: ${task.exception}")
        throw Exception("Error fetching user: ${task.exception}")
      }
    }
  }

  /**
   * Stores/updates a user in the Firestore database.
   *
   * @param user The user to store.
   * @return A Task that completes when the user has been stored.
   * @throws Exception If an error occurs while storing the user.
   */
  fun storeUser(user: User): Task<Unit> {
    val userData = user.serialize()
    return userCollection.document(user.id).set(userData).continueWith { task ->
      if (!task.isSuccessful) {
        val exception = task.exception
        Log.e("UserFirebaseRepository", "Error adding user: $exception")
        throw Exception("Error adding user: $exception")
      } else {
        Log.d("UserFirebaseRepository", "User Stored Successfully")
      }
    }
  }
}
