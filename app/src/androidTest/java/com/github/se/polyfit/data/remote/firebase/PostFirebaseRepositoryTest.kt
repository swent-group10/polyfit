package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import kotlin.test.assertFailsWith
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

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
  fun storePostReturnDocumentReferenceOnSuccess() = runTest {
    val mockDocRef = mockk<DocumentReference>()
    val task = Tasks.forResult(mockDocRef)

    coEvery { mockCollectionRef.add(testPost.serialize()) } returns task

    val result = postFirebaseRepository.storePost(testPost).await()

    assertEquals(mockDocRef, result)
  }

  @Test
  fun storePostShouldThrowExceptionOnFailure() = runTest {
    val mockTask: Task<DocumentReference> = mockk()

    coEvery { mockCollectionRef.add(testPost.serialize()) } returns mockTask
    every { mockTask.isSuccessful } returns true

    assertFailsWith<Exception> { postFirebaseRepository.storePost(testPost).await() }
  }
}