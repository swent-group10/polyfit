
package com.github.se.polyfit.data.remote.firebase

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GeoQueryRepositoryTest {

  private val mockRtdb: FirebaseDatabase = mockk()
  private val mockGeoFireRef: DatabaseReference = mockk()
  private lateinit var repository: PostFirebaseRepository

  @Before
  fun setUp() {
    every { mockRtdb.getReference("posts_location") } returns mockGeoFireRef
    every { mockGeoFireRef.orderByChild(any()) } returns mockGeoFireRef

    every { mockGeoFireRef.orderByChild("someKey").equalTo("someValue" as String?) } returns
        mockGeoFireRef
    every { mockGeoFireRef.startAt(any<String>()) } returns mockGeoFireRef
    every { mockGeoFireRef.endAt(any<String>()) } returns mockGeoFireRef

    every { mockGeoFireRef.equalTo("someStringValue" as String?) } returns mockGeoFireRef
    every { mockGeoFireRef.addChildEventListener(any()) } returns mockk()

    every { mockGeoFireRef.addListenerForSingleValueEvent(any()) } just Runs

    repository = PostFirebaseRepository(rtdb = mockRtdb)
  }

  @Test
  fun queryNearbyPostsShouldTriggerGeoQueryEventListener() = runBlocking {
    val mockGeoFire = mockk<GeoFire>()
    val mockGeoQuery = mockk<GeoQuery>(relaxed = true)
    coEvery {
      mockGeoFire.queryAtLocation(
          match { it.latitude == 37.7749 && it.longitude == -122.4194 }, eq(10.0))
    } returns mockGeoQuery

    val listenerSlot = slot<GeoQueryEventListener>()
    every { mockGeoQuery.addGeoQueryEventListener(capture(listenerSlot)) } answers {}

    repository.queryNearbyPosts(37.7749, -122.4194, 10.0, mockGeoFire)

    coVerify(exactly = 1) {
      mockGeoFire.queryAtLocation(
          match { it.latitude == 37.7749 && it.longitude == -122.4194 }, eq(10.0))
    }

    listenerSlot.captured.onKeyEntered("key1", GeoLocation(37.7749, -122.4194))
    listenerSlot.captured.onGeoQueryReady()
  }

  @Test
  fun queryNearby() = runBlocking {
    val mockGeoFire = mockk<GeoFire>()
    val mockGeoQuery = mockk<GeoQuery>(relaxed = true)
    coEvery {
      mockGeoFire.queryAtLocation(
          match { it.latitude == 37.7749 && it.longitude == -122.4194 }, eq(10.0))
    } returns mockGeoQuery

    repository.queryNearbyPosts(37.7749, -122.4194, 10.0, mockGeoFire)

    coVerify(exactly = 1) {
      mockGeoFire.queryAtLocation(
          match { it.latitude == 37.7749 && it.longitude == -122.4194 }, eq(10.0))
    }
  }

  @Test
  fun geoQueryEventListenerShouldHandleKeyEnteredAndExited() = runBlocking {
    val mockGeoFire = mockk<GeoFire>()
    val mockGeoQuery = mockk<GeoQuery>(relaxed = true)
    coEvery {
      mockGeoFire.queryAtLocation(
          match { it.latitude == 37.7749 && it.longitude == -122.4194 }, eq(10.0))
    } returns mockGeoQuery

    val listenerSlot = slot<GeoQueryEventListener>()
    every { mockGeoQuery.addGeoQueryEventListener(capture(listenerSlot)) } answers {}

    repository.queryNearbyPosts(37.7749, -122.4194, 10.0, mockGeoFire)

    listenerSlot.captured.onKeyEntered("key1", GeoLocation(37.7749, -122.4194))
    listenerSlot.captured.onKeyExited("key1")
  }

  @Test
  fun geoQueryEventListenerShouldHandleGeoQueryReady() = runBlocking {
    val mockGeoFire = mockk<GeoFire>()
    val mockGeoQuery = mockk<GeoQuery>(relaxed = true)
    coEvery {
      mockGeoFire.queryAtLocation(
          match { it.latitude == 37.7749 && it.longitude == -122.4194 }, eq(10.0))
    } returns mockGeoQuery

    val listenerSlot = slot<GeoQueryEventListener>()
    every { mockGeoQuery.addGeoQueryEventListener(capture(listenerSlot)) } answers {}

    repository.queryNearbyPosts(37.7749, -122.4194, 10.0, mockGeoFire)

    listenerSlot.captured.onGeoQueryReady()
  }

  @Test
  fun geoQueryEventListenerShouldHandleGeoQueryError() = runBlocking {
    val mockGeoFire = mockk<GeoFire>()
    val mockGeoQuery = mockk<GeoQuery>(relaxed = true)
    coEvery {
      mockGeoFire.queryAtLocation(
          match { it.latitude == 37.7749 && it.longitude == -122.4194 }, eq(10.0))
    } returns mockGeoQuery

    val listenerSlot = slot<GeoQueryEventListener>()
    every { mockGeoQuery.addGeoQueryEventListener(capture(listenerSlot)) } answers {}

    repository.queryNearbyPosts(37.7749, -122.4194, 10.0, mockGeoFire)

    listenerSlot.captured.onGeoQueryError(DatabaseError.fromCode(DatabaseError.UNKNOWN_ERROR))
  }
}

