package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.data.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class UserFirebaseRepository(db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
  private val userCollection = db.collection("users")

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
