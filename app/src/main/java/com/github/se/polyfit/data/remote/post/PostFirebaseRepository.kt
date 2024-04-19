package com.github.se.polyfit.data.remote.post

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class PostFirebaseRepository(db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
  private val postCollection = db.collection("posts")

  fun storePost(post: Post): Task<Unit> {
    return postCollection.add(post.serialize()).continueWithTask {
      if (it.isSuccessful) {
        it.result.get()
      } else {}
    }
  }

  fun addSnapshotListener() {}

  fun getAllPosts(): LiveData<List<Post>> {
    val allPost: MutableLiveData<List<Post>> = MutableLiveData()

    postCollection.get().continueWith { task ->
      if (task.isSuccessful) {
        try {
          val posts =
              task.result.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.data?.let { Post.deserialize(it) }
              }
          allPost.value = posts
        } catch (e: Exception) {
          Log.e("PostFirebaseRepository", "Error processing post documents", e)
          throw Exception("Error processing post documents: ${e.message}")
        }
      } else {
        Log.e("PostFirebaseRepository", "Failed to fetch meals: ${task.exception?.message}")
        throw Exception("Failed to fetch post: ${task.exception?.message}")
      }
    }

    return allPost
  }
}
