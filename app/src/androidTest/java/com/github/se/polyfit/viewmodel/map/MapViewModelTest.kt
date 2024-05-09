package com.github.se.polyfit.viewmodel.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MapViewModelTest {

  private val repository = mockk<PostFirebaseRepository>()
  private lateinit var viewModel: MapViewModel

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    viewModel = MapViewModel(repository)
  }

  @Test
  fun setRadiusUpdatesRadiusLiveData() = runTest {
    val expectedRadius = 6.0

    viewModel.setRadius(expectedRadius)

    assertEquals(expectedRadius, viewModel.radius.value)
  }

    @Test
    fun setNegativeRadius() = runTest {
        val expectedRadius = 0.0
        val negativeRadius = -1.0

        viewModel.setRadius(negativeRadius)

        assertEquals(expectedRadius, viewModel.radius.value)
    }

  @Test
  fun setLocationUpdatesRadiusLiveData() = runTest {
    val location: Location = Location(1.0, 1.0, 0.0, "")

    viewModel.setLocation(location)

    assertEquals(location, viewModel.location.value)
  }

  @Test
  fun listenToPostsUpdatesPostsLiveData() = runTest {
    val expectedPosts =
        listOf(
            Post(
                "1",
                "Title1",
                Location(1.0, 1.0, 1.0, "TestAdress1"),
                Meal.default(),
                LocalDate.now()),
            Post(
                "2",
                "Title2",
                Location(2.0, 2.0, 2.0, "TestAdress1"),
                Meal.default(),
                LocalDate.now()))

    val radius = 5.0

    val location: Location = Location(1.0, 1.0, 0.0, "")

    coEvery {
      repository.queryNearbyPosts(
          centerLatitude = any(),
          centerLongitude = any(),
          radiusInKm = any(),
          completion = any(),
          geoFire = any())
    } answers
        {
          val callback = args[3] as (List<Post>) -> Unit
          callback(expectedPosts)
        }

    viewModel.setLocation(location)
    viewModel.setRadius(radius)
    viewModel.listenToPosts()

    assertEquals(expectedPosts, viewModel.posts.value)
  }
}
