package com.github.se.polyfit.data.remote.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.github.se.polyfit.model.post.Post
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostFirebaseRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val pictureDb: FirebaseStorage = FirebaseStorage.getInstance(),
    private val rtdb: FirebaseDatabase =
        FirebaseDatabase.getInstance(
            "https://polyfit-316e8-default-rtdb.europe-west1.firebasedatabase.app/")
) {
  private val postCollection = db.collection("posts")
  private val geoFireRef = rtdb.getReference("posts_location")
  private val geoFire = GeoFire(geoFireRef)

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

  /**
   * Initiates a query to find posts within a specified radius around a given center latitude and
   * longitude. The results are provided asynchronously via a GeoQueryEventListener.
   *
   * This function sets up a GeoQuery to listen for events related to the specified geographical
   * area. It reacts to posts that enter, exit, or move within this area.
   *
   * The nearby post IDs are accumulated and once the initial set of data is loaded
   * (onGeoQueryReady), it triggers a fetch for the actual post data from Firestore.
   *
   * @param centerLatitude The latitude of the center point for the query.
   * @param centerLongitude The longitude of the center point for the query.
   * @param radiusInKm The radius around the center point in kilometers within which to search for
   *   posts.
   */
  fun queryNearbyPosts(
      centerLatitude: Double,
      centerLongitude: Double,
      radiusInKm: Double,
      completion: (List<Post>) -> Unit,
      geoFire: GeoFire = GeoFire(geoFireRef)
  ) {
    val center = GeoLocation(centerLatitude, centerLongitude)
    val query = geoFire.queryAtLocation(center, radiusInKm)

    query.addGeoQueryEventListener(
        object : GeoQueryEventListener {
          val nearbyKeys = mutableListOf<String>()

          override fun onKeyEntered(key: String, location: GeoLocation) {
            nearbyKeys.add(key)
          }

          override fun onKeyExited(key: String) {
            nearbyKeys.remove(key)
          }

          override fun onKeyMoved(key: String, location: GeoLocation) {
            // Do nothing but need to be there
          }

          override fun onGeoQueryReady() {
            PostFirebaseRepository().fetchPostsAndImages(nearbyKeys, completion = completion)
          }

          override fun onGeoQueryError(error: DatabaseError) {
            Log.e("GeoQuery", "There was an error with the geo query: ${error.message}")
          }
        })
  }

  fun fetchPostsAndImages(
      keys: List<String>,
      postCollection: CollectionReference = this.postCollection,
      completion: (List<Post>) -> Unit
  ) {
    val posts = mutableListOf<Post>()
    val batchSize = 10
    val batches = keys.chunked(batchSize)
    var completedBatches = 0

    // Use a batch operation to fetch all posts
    batches.forEach { batch ->
      postCollection
          .whereIn(FieldPath.documentId(), batch)
          .get()
          .addOnSuccessListener { querySnapshot ->
            CoroutineScope(Dispatchers.Default).launch {
              val tempPosts = mutableListOf<Post>()
              val deferredImages = mutableListOf<Deferred<Unit>>()

              querySnapshot.documents.forEach { document ->
                document.data?.let {
                  Post.deserialize(it)?.also { post ->
                    deferredImages.add(
                        async {
                          post.listOfURLs = fetchImageReferencesForPost(document.id)

                          post.imageDownloadURL =
                              post.listOfURLs.map { it.downloadUrl.await() }.first()

                          Log.d("PostFirebase", "download uri : ${post.imageDownloadURL}")

                          return@async
                        })
                    tempPosts.add(post)
                  }
                }
              }
              deferredImages.awaitAll()

              synchronized(posts) {
                posts.addAll(tempPosts)
                for (post in posts) {
                  Log.d("PostFirebase", "download uri : ${post.imageDownloadURL}")
                }
                completedBatches++
                if (completedBatches == batches.size) {
                  completion(posts)
                }
              }
            }
          }
          .addOnFailureListener {
            Log.e("PostFirebaseRepository", "Failed to fetch posts: ${it.message}")
          }
    }
  }

  suspend fun fetchImageReferencesForPost(postKey: String): List<StorageReference> {
    return withContext(Dispatchers.Default) {
      val storageRef = pictureDb.getReference("posts/$postKey")
      val listResult = storageRef.listAll().await()
      listResult.items
    }
  }

  suspend fun uploadImage(
      image: Bitmap,
      //        documentRef: DocumentReference
  ): Uri? {
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()
    val stream = ByteArrayInputStream(data)

    val path = "${UUID.randomUUID()}.jpg"
    val refSource = pictureDb.getReference(path)

    try {
      val uploadTask = refSource.putStream(stream)
      return uploadTask.await().storage.downloadUrl.await()
    } catch (e: Exception) {
      Log.e("PostFirebaseRepository", "Failed to upload image", e)
      throw Exception("Error uploading images : ${e.message}", e)
    }
  }
}
