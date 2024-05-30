package com.github.se.polyfit.viewmodel.post



/*
import androidx.lifecycle.MutableLiveData
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Post
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ViewPostViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @MockK private lateinit var postFirebaseRepository: PostFirebaseRepository

  @MockK private lateinit var postLocalRepository: PostLocationModel

  private lateinit var viewModel: ViewPostViewModel

  @Before
  fun setup() {
    MockKAnnotations.init(this, relaxed = true)
    postFirebaseRepository = mockk(relaxed = true)
    postLocalRepository = mockk(relaxed = true)
    coEvery { postLocalRepository.getCurrentLocation(any()) } returns
        Location(latitude = 46.5181, longitude = 6.5659, altitude = 0.0, name = "")
    coEvery { postFirebaseRepository.queryNearbyPosts(any(), any(), any(), any()) } answers
        {
          val completion = arg<(List<Post>) -> Unit>(3)
          completion(
              listOf(
                  Post(
                      "1",
                      "Title",
                      Location(latitude = 46.5181, longitude = 6.5659, altitude = 0.0, name = ""),
                      Meal.default(),
                      LocalDate.now())))
        }

    viewModel = ViewPostViewModel(postFirebaseRepository, postLocalRepository)
  }

  /* @Test
  fun getAllPost_returnsCorrectly() = runTest {
    // Act

    viewModel.getNearbyPosts()
    delay(1000) // Allow time for the coroutine to complete
=======
    viewModel.getAllPost()


    // Assert
    coVerify { postFirebaseRepository.posts }
  }

  @Test
  fun returnsEmptyListWhenNoPostsAvailable() = runTest {

    every { postFirebaseRepository.getAllPosts() } returns flowOf(emptyList())

    viewModel.getNearbyPosts()

    val result = viewModel.posts.value
    assertEquals(emptyList<Post>(), result)
  }*/

  @Test
  fun testGetNearbyPosts() = runTest {
    // Observe changes to posts and isFetching
    val postsObserver = mockk<Observer<List<Post>>>(relaxed = true)
    val fetchingObserver = mockk<Observer<Boolean>>(relaxed = true)
    val locationObserver = mockk<Observer<Location>>(relaxed = true)

    viewModel.posts.asLiveData().observeForever(postsObserver)
    viewModel.isFetching.asLiveData().observeForever(fetchingObserver)
    viewModel.location.observeForever(locationObserver)

    // Trigger getNearbyPosts function
    viewModel.getNearbyPosts()

    delay(4000)

    // Verify location fetched
    verify {
      locationObserver.onChanged(
          Location(latitude = 46.5181, longitude = 6.5659, altitude = 0.0, name = ""))
    }


    // Verify posts fetched
    verify {
      postsObserver.onChanged(
          listOf(
              Post(
                  "1",
                  "Title",
                  Location(latitude = 46.5181, longitude = 6.5659, altitude = 0.0, name = ""),
                  Meal.default(),
                  LocalDate.now())))
    }

    // Verify that the repository methods are called
    coVerify { postLocalRepository.getCurrentLocation(any()) }
    coVerify { postFirebaseRepository.queryNearbyPosts(46.5181, 6.5659, 1.0, any()) }

    every { postFirebaseRepository.posts } returns MutableLiveData(emptyList())
    val result = viewModel.getAllPost()

    assertEquals(emptyList<Post>(), result.value)

  }
}
*/
