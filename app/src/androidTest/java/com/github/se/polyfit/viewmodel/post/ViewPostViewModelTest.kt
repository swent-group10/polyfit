package com.github.se.polyfit.viewmodel.post

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

  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private lateinit var viewModel: ViewPostViewModel

  @Before
  fun setup() {
    postFirebaseRepository = mockk(relaxed = true)
    viewModel = ViewPostViewModel(postFirebaseRepository)
  }

  @Test
  fun getAllPost_returnsCorrectly() = runTest {
    // Act
    viewModel.getAllPost()

    // Assert
    coVerify { postFirebaseRepository.posts }
  }

  @Test
  fun returnsEmptyListWhenNoPostsAvailable() = runTest {
    every { postFirebaseRepository.posts } returns MutableLiveData(emptyList())
    val result = viewModel.getAllPost()

    assertEquals(emptyList<Post>(), result.value)
  }
}
