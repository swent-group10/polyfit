package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.github.se.polyfit.model.post.Post
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PostFirebaseRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val pictureDb: FirebaseStorage = FirebaseStorage.getInstance()
) {
  private val postCollection = db.collection("posts")

  suspend fun storePost(post: Post): DocumentReference? {
    try {
      val documentRef = postCollection.add(post.serialize()).await()

      return documentRef
    } catch (e: Exception) {
      Log.e("PostFirebaseRepository", "Failed to store post in the database", e)
      throw Exception("Error uploading images : ${e.message}", e)
    }
  }

  fun getAllPosts(): Flow<List<Post>> = flow {
    val posts = mutableListOf<Post>()
    val batchSize = 10
    var itr = 0

    val postList =
        postCollection.get().await().map { document ->
          Post.deserialize(document.data)?.let { posts.add(it) }
          itr++
        }

    emit(posts)
  }
}
