package com.github.se.polyfit.data.remote.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostFirebaseRepository(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    rtdb: FirebaseDatabase =
        FirebaseDatabase.getInstance(
            "https://polyfit-316e8-default-rtdb.europe-west1.firebasedatabase.app/"),
    private val pictureDb: FirebaseStorage = FirebaseStorage.getInstance()
) {
  private val postCollection = db.collection("posts")
  private val geoFireRef = rtdb.getReference("posts_location")
  private val geoFire = GeoFire(geoFireRef)

  private val _posts: MutableLiveData<List<Post>> = MutableLiveData()
  val posts: LiveData<List<Post>> = _posts

  /**
   * Initializes a listener that listens for changes in the database and updates the LiveData
   * object. This is implemented this way to allow for the scope to passed in from the ViewModel.
   *
   * @param scope The CoroutineScope in which to launch the listener.
   */
  fun initializeFirebaseListener(scope: CoroutineScope) {
    postCollection.addSnapshotListener { _, _ -> scope.launch { getAllPosts() } }
  }

  suspend fun storePost(post: Post): DocumentReference? {
    try {
      val documentRef = postCollection.add(post.serialize()).await()

      geoFire.setLocation(
          documentRef.id, GeoLocation(post.location.latitude, post.location.longitude))

      return documentRef
    } catch (e: Exception) {
      Log.e("PostFirebaseRepository", "Failed to store post in the database", e)
      throw Exception("Error uploading images : ${e.message}", e)
    }
  }

  /** Fetches all posts from the database and updates the LiveData object with the new data. */
  suspend fun getAllPosts(): MutableList<Post> {
    val newPosts: MutableList<Post> = mutableListOf()

    try {

      postCollection
          .get()
          .await()
          .map { document -> Post.deserialize(document.data)?.let { newPosts.add(it) } }
          .also { _posts.postValue(newPosts) }

      return newPosts
    } catch (e: Exception) {
      Log.e("PostFirebaseRepository", "Failed to get posts from the database", e)
      throw Exception("Error getting posts : ${e.message}", e)
    }
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

              querySnapshot.documents.forEach { document ->
                document.data?.let { Post.deserialize(it)?.also { post -> tempPosts.add(post) } }
              }

              synchronized(posts) {
                posts.addAll(tempPosts)

                completedBatches++
                if (completedBatches == batches.size) {
                  completion(posts)
                }
              }
            }
          }
          .addOnSuccessListener { Log.d("PostFirebaseRepository", "Successfully fetched posts") }
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
