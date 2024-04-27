package com.github.se.polyfit.data.remote.firebase

import android.util.Log
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.github.se.polyfit.BuildConfig
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class GeoQueryRepository(
    rtdb: FirebaseDatabase =
        FirebaseDatabase.getInstance(BuildConfig.RTDB_URL)
) {
  private val geoFireRef = rtdb.getReference("posts_location")

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
      geoFire: GeoFire = GeoFire(geoFireRef)
  ) {
    val center = GeoLocation(centerLatitude, centerLongitude)
    val query = geoFire.queryAtLocation(center, radiusInKm)
    Log.d("GeoQuery", "Querying posts within $radiusInKm km of $center")
    query.addGeoQueryEventListener(
        object : GeoQueryEventListener {
          val nearbyKeys = mutableListOf<String>()

          override fun onKeyEntered(key: String, location: GeoLocation) {
            Log.d("GeoQuery", "Key entered: $key")
            nearbyKeys.add(key)
          }

          override fun onKeyExited(key: String) {
            Log.d("GeoQuery", "Key exited: $key")

            nearbyKeys.remove(key)
          }

          override fun onKeyMoved(key: String, location: GeoLocation) {

            // Do nothing
          }

          override fun onGeoQueryReady() {
            PostFirebaseRepository().fetchPosts(nearbyKeys) { posts ->
              // Do something with the fetched posts
            }
          }

          override fun onGeoQueryError(error: DatabaseError) {

            Log.e("GeoQuery", "There was an error with the geo query: ${error.message}")
          }
        })
  }
}
