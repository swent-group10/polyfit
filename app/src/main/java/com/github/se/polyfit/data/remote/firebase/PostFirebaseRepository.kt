package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class PostFirebaseRepository(db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
  private val postCollection = db.collection("posts")

  fun storePost(post: Post): Task<DocumentReference> {
    return postCollection.add(post.serialize()).continueWithTask {
      if (it.isSuccessful) {
        return@continueWithTask it
      } else {
        Log.e("PostFirebaseRepository", "Failed to store post in the database : ${it.exception}")
        throw Exception("Failed to store post in the database : ${it.exception}")
      }
    }
  }
}
