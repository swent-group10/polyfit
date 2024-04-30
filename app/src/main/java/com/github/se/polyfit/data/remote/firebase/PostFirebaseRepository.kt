package com.github.se.polyfit.data.remote.firebase

import android.graphics.Bitmap
import android.util.Log
import com.github.se.polyfit.model.post.Post
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlinx.coroutines.tasks.await

class PostFirebaseRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val pictureDb: FirebaseStorage = FirebaseStorage.getInstance()
) {
  private val postCollection = db.collection("posts")

  suspend fun storePost(post: Post): List<StorageReference> {
    try {
      val documentRef = postCollection.add(post.serialize()).await()
      var listTaskSnapshot: List<StorageReference> = listOf()
      if (post.listOfImages.isNotEmpty()) {
        listTaskSnapshot = post.listOfImages.map { uploadImage(it, documentRef) }
      }
      return listTaskSnapshot
    } catch (e: Exception) {
      Log.e("PostFirebaseRepository", "Failed to store post in the database", e)
      throw Exception("Error uploading images : ${e.message}", e)
    }
  }

  private suspend fun uploadImage(image: Bitmap, documentRef: DocumentReference): StorageReference {
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()
    val stream = ByteArrayInputStream(data)

    val path = "${documentRef.path}/${UUID.randomUUID()}.jpg"
    val refSource = pictureDb.getReference(path)

    try {
      val uploadTask = refSource.putStream(stream)
      return uploadTask.await().storage
    } catch (e: Exception) {
      Log.e("PostFirebaseRepository", "Failed to upload image", e)
      throw Exception("Error uploading images : ${e.message}", e)
    }
  }
}
