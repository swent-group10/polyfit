package com.github.se.polyfit.data.remote.firebase

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class GeoQueryRepository(
    rtdb: FirebaseDatabase = FirebaseDatabase.getInstance("your_realtime_database_url")
) {
  private val geoFireRef = rtdb.getReference("posts_location")
  private val geoFire = GeoFire(geoFireRef)

  /**
   * Initiates a query to find posts within a specified radius around a given center latitude and longitude.
   * The results are provided asynchronously via a GeoQueryEventListener.
   *
   * This function sets up a GeoQuery to listen for events related to the specified geographical area.
   * It reacts to posts that enter, exit, or move within this area.
   *
   * The nearby post IDs are accumulated and once the initial set of data is loaded (onGeoQueryReady),
   * it triggers a fetch for the actual post data from Firestore.
   *
   * @param centerLatitude The latitude of the center point for the query.
   * @param centerLongitude The longitude of the center point for the query.
   * @param radiusInKm The radius around the center point in kilometers within which to search for posts.
   */

  fun queryNearbyPosts(centerLatitude: Double, centerLongitude: Double, radiusInKm: Double) {
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

            // Do nothing
          }

          override fun onGeoQueryReady() {

            PostFirebaseRepository().fetchPosts(nearbyKeys) { posts ->
              // Do something with the fetched posts
            }
          }

          override fun onGeoQueryError(error: DatabaseError) {

            println("There was an error with the geo query: ${error.message}")
          }
        })
  }
}
