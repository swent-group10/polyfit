package com.github.se.polyfit.data.remote.firebase

import android.graphics.Bitmap
import android.util.Log
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.UUID

class PostFirebaseRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val pictureDb: FirebaseStorage = FirebaseStorage.getInstance()
) {
    private val postCollection = db.collection("posts")

    fun storePost(post: Post): Task<Void> {
        val documentTask = postCollection.add(post.serialize())

        return documentTask.continueWithTask { task ->
            if (task.isSuccessful) {
                val documentRef = task.result
                if (post.listOfImages.isNotEmpty()) {
                    val uploadTask = post.listOfImages.map {
                        uploadImage(it, documentRef)
                    }

                    Tasks.whenAll(uploadTask)

                } else {
                    Tasks.forResult(null) // No images to upload, return null
                }
            } else {
                Log.e(
                    "PostFirebaseRepository", "Failed to store post in the database", task.exception
                )
                task.exception?.let { Tasks.forException(it) }
            }

        }
    }


    private fun uploadImage(image: Bitmap, documentRef: DocumentReference): Task<Void> {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val stream = ByteArrayInputStream(data)

        val path = "${documentRef.path}/${UUID.randomUUID()}.jpg"
        val refSource = pictureDb.getReference(path)
        return refSource.putStream(stream).continueWithTask { uploadTask ->
            if (uploadTask.isSuccessful) {
                Log.d("PostFirebaseRepository", "Image uploaded successfully")
                Tasks.forResult(null) // Return null to signify completion
            } else {
                Log.e(
                    "PostFirebaseRepository", "Failed to upload image", uploadTask.exception
                )
                uploadTask.exception?.let { Tasks.forException(it) }
            }

        }
    }


}