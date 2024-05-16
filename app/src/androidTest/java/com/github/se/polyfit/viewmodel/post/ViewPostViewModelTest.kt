package com.github.se.polyfit.viewmodel.post

import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class ViewPostViewModelTest {

  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private lateinit var viewModel: ViewPostViewModel

  @Before
  fun setup() {
    postFirebaseRepository = mockk(relaxed = true)
    viewModel = ViewPostViewModel(postFirebaseRepository)
  }

  @Test
  fun getAllPost_returnsCorrectly() = runTest {
    // Arrange
    val posts = listOf(Post("1", "Title", Location.default(), Meal.default(), LocalDate.now()))
    coEvery { postFirebaseRepository.getAllPosts() } returns flowOf(posts)

    // Act
    viewModel.getAllPost()
    delay(1000) // Allow time for the coroutine to complete

    // Assert
    coVerify { postFirebaseRepository.getAllPosts() }
    assertEquals(posts, viewModel.posts.value)
    assertFalse(viewModel.isFetching.value)
  }

  @Test
  fun returnsEmptyListWhenNoPostsAvailable() = runTest {
    every { postFirebaseRepository.getAllPosts() } returns flowOf(emptyList())

    viewModel.getAllPost()

    val result = viewModel.posts.value
    assertEquals(emptyList<Post>(), result)
  }
}
