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

    try {

      postCollection.get().await().map { document ->
        Log.d("PostFirebaseRepository", "Document: ${document.data}")
        Post.deserialize(document.data)?.let { posts.add(it) }
      }
    } catch (e: Exception) {
      Log.e("PostFirebaseRepository", "Failed to get posts from the database", e)
      throw Exception("Error getting posts : ${e.message}", e)
    }
    emit(posts)
  }
}
