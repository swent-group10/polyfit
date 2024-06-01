package com.github.se.polyfit.viewmodel.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.PostLocationModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Priority
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ViewPostViewModelTest {

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private lateinit var viewModelMockk: ViewPostViewModel

  private lateinit var viewModel: ViewPostViewModel
  private lateinit var postLocationModel: PostLocationModel
  val default = 0.0

  private val expectedLocation = Location(default, default, default, "Test Location")

  @Before
  fun setup() {
    postFirebaseRepository = mockk(relaxed = true)

    postLocationModel = mockk(relaxed = true)
    viewModel = ViewPostViewModel(postFirebaseRepository, postLocationModel)

    viewModelMockk = mockk<ViewPostViewModel>()
    every { viewModelMockk.location.value } returns expectedLocation
  }

  @Test
  fun testInitBlock(): Unit = runTest {
    // Arrange

    coEvery {
      postLocationModel.getCurrentLocation(
          CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build())
    } returns expectedLocation

    assertTrue(viewModel.isFetching.value!!)
    assertEquals(expectedLocation, viewModelMockk.location.value)
  }
}
