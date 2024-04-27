package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import kotlin.test.assertFailsWith
import kotlin.test.fail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.fail

class PostFirebaseRepositoryTest {

  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private val mockDb: FirebaseFirestore = mockk()
  private val mockCollectionRef = mockk<CollectionReference>()

  private val testPost =
      Post(
          "userId",
          "description",
          Location(0.0, 0.0, 10.0, "EPFL"),
          Meal.default(),
          LocalDate.now())

  @Before
  fun setUp() {
    every { mockDb.collection("posts") } returns mockCollectionRef
    postFirebaseRepository = PostFirebaseRepository(mockDb)
  }

  @Test
  fun storePostShouldThrowExceptionOnFailure() = runTest {
    val mockTask: Task<DocumentReference> = mockk()

    coEvery { mockCollectionRef.add(testPost.serialize()) } returns mockTask
    every { mockTask.isSuccessful } returns true

    assertFailsWith<Exception> { postFirebaseRepository.storePost(testPost).await() }
  }

  @Test
  fun storeDefaultPostInDatabase() = runBlocking {
    val post = Post.default()
    val postFirebaseRepository = PostFirebaseRepository()
    try {
      postFirebaseRepository.storePost(post).await()
      // If we get here, the task was successful
      assertTrue(true)
    } catch (exception: Exception) {
      // If the task fails, the test should fail
      fail("Failed to store post: ${exception.message}")
    }
  }

  @Test
  fun storePostsInDatabase() = runBlocking {
    val post =
        Post(
            "userId",
            "description",
            Location(-110.0, 40.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now())
    val postFirebaseRepository = PostFirebaseRepository()
    try {
      postFirebaseRepository.storePost(post).await()
      // If we get here, the task was successful
      assertTrue(true)
    } catch (exception: Exception) {
      // If the task fails, the test should fail
      fail("Failed to store post: ${exception.message}")
    }
  }
}
