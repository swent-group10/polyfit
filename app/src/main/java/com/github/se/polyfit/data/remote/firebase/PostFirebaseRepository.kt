package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.github.se.polyfit.BuildConfig
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class PostFirebaseRepository(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    rtdb: FirebaseDatabase = FirebaseDatabase.getInstance(BuildConfig.RTDB_URL)
) {
  private val postCollection = db.collection("posts")
  private val geoFireRef = rtdb.getReference("posts_location")
  private val geoFire = GeoFire(geoFireRef)

  /**
   * Stores a post into the Firestore database and sets its geolocation in the Realtime Database.
   *
   * This function serializes the Post object and stores it in the Firestore 'posts' collection.
   * Upon successful storage, it retrieves the auto-generated document ID and uses it to set the
   * geolocation of the post in the Realtime Database using GeoFire. The completion of the operation
   * (both Firestore storage and GeoFire location setting) is signaled by completing a Task<Void>.
   *
   * If storing the post in Firestore succeeds, but setting the location with GeoFire fails, the
   * task is completed exceptionally with an error message indicating the failure to store the post
   * location. If storing the post in Firestore fails, the task is also completed exceptionally with
   * the corresponding exception.
   *
   * @param post The post object to be stored.
   * @return A Task<Void> that is completed when the post is stored and its location is set. If an
   *   error occurs, the Task is completed exceptionally.
   */
  fun storePost(post: Post): Task<Void> {
    val completionSource = TaskCompletionSource<Void>()
    val addPostTask = postCollection.add(post.serialize())

    addPostTask
        .addOnSuccessListener { documentReference ->
          val postId = documentReference.id
          val geoLocation = GeoLocation(post.location.latitude, post.location.longitude)

          geoFire.setLocation(postId, geoLocation) { _, error ->
            if (error != null) {
              Log.e("PostFirebaseRepository", "Failed to store post location: $error")
              completionSource.setException(Exception("Failed to store post location: $error"))
            } else {
              completionSource.setResult(null)
            }
          }
        }
        .addOnFailureListener { exception -> completionSource.setException(exception) }

    return completionSource.task
  }

  /**
   * Retrieves a list of posts from Firestore based on a provided list of document keys.
   *
   * This function performs batched reads for efficiency, dividing the provided keys into chunks and
   * querying Firestore for each chunk. It compiles the results into a list of Post objects and
   * invokes the provided completion handler with this list.
   *
   * The function ensures all requested posts are fetched before calling the completion handler. If
   * a document cannot be deserialized into a Post object (for example, if required fields are
   * missing), that document is skipped.
   *
   * The batch size is determined by the Firestore limit for 'whereIn' queries.
   *
   * @param keys A list of Firestore document keys representing the posts to fetch.
   * @param completion A handler to be called with the resulting list of Post objects once all
   *   batches have been processed.
   */
  fun fetchPosts(keys: List<String>, completion: (List<Post>) -> Unit) {
    val posts = mutableListOf<Post>()
    val batchSize = 10
    val batches = keys.chunked(batchSize)
    var itr = 0

    // Use a batch operation to fetch all posts
    for (batch in batches) {
      postCollection
          .whereIn(FieldPath.documentId(), batch)
          .get()
          .addOnSuccessListener { querySnapshot ->
            querySnapshot.documents.forEach { document ->
              val post = Post.deserialize(document.data ?: return@forEach)
              itr++
              if (post != null) {
                posts.add(post)
              } else {
                return@forEach
              }
            }
            if (itr == batches.size) {
              completion(posts)
            }
          }
          .addOnFailureListener() {
            Log.e("PostFirebaseRepository", "Failed to fetch posts: ${it.message}")
          }
    }
  }
}
