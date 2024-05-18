package com.github.se.polyfit.data.remote.firebase

import android.graphics.Bitmap
import android.net.Uri
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import junit.framework.TestCase.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostFirebaseRepositoryTest {

  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private val mockDb: FirebaseFirestore = mockk(relaxed = true)
  private val mockCollectionRef = mockk<CollectionReference>(relaxed = true)
  private val mockPictureDb: FirebaseStorage = mockk(relaxed = true)

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
    postFirebaseRepository = PostFirebaseRepository(mockDb, mockPictureDb)
  }

  @Test
  fun storePostReturnDocumentReferenceOnSuccess() = runTest {
    val mockDocRef = mockk<DocumentReference>()
    val task = Tasks.forResult(mockDocRef)

    coEvery { mockCollectionRef.add(testPost.serialize()) } returns task
    every { mockDocRef.id } returns "mockId"

    val result = postFirebaseRepository.storePost(testPost)

    assertEquals(mockDocRef, result)
  }

  @Test
  fun storePostShouldThrowExceptionOnFailure() = runTest {
    coEvery { mockCollectionRef.add(testPost.serialize()) } returns
        Tasks.forException(Exception("Error adding document"))

    assertFailsWith<Exception> { runBlocking { postFirebaseRepository.storePost(testPost) } }
  }

  @Test
  fun getAllPostsReturnsCorrectPosts() = runTest {
    // Arrange
    val post = Post("1", "Title", Location.default(), Meal.default(), LocalDate.now())

    val mockQuerySnapshot: QuerySnapshot = mockk(relaxed = true)
    val mockDocumentSnapshot: DocumentSnapshot = mockk()

    every { mockDocumentSnapshot.data } returns Post.serialize(post)
    every { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)
    coEvery { mockCollectionRef.get() } returns Tasks.forResult(mockQuerySnapshot)

    // Act
    val postsFlow = postFirebaseRepository.getAllPosts().toList()

    delay(1000)

    // Assert
    assert(postsFlow.isNotEmpty())
  }

  @Test
  fun uploadImageReturnsCorrectUri() = runTest {
    val testBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    val testUri = Uri.parse("https://test.com/image.jpg")

    val mockUploadTask = mockk<UploadTask>()
    val mockStorageRef = mockk<StorageReference>(relaxed = true)
    every { mockPictureDb.getReference(any()) } returns mockStorageRef
    every { mockStorageRef.putStream(any()) } returns mockUploadTask
    every { mockUploadTask.isComplete } returns true
    every { mockUploadTask.exception } returns null
    every { mockUploadTask.isCanceled } returns false
    val mockResult = mockk<UploadTask.TaskSnapshot>()
    every { mockUploadTask.getResult() } returns mockResult
    every { mockResult.storage.downloadUrl } returns Tasks.forResult(testUri)
    val result = postFirebaseRepository.uploadImage(testBitmap)

    assertEquals(testUri, result)
  }

  @Test
  fun uploadImageThrowsExceptionOnFailure() = runTest {
    val testBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    val mockUploadTask = mockk<UploadTask>()
    val mockStorageRef = mockk<StorageReference>(relaxed = true)
    every { mockPictureDb.getReference(any()) } returns mockStorageRef
    every { mockStorageRef.putStream(any()) } returns mockUploadTask
    every { mockUploadTask.isComplete } returns true
    every { mockUploadTask.exception } returns Exception("Error uploading image")

    assertFailsWith<Exception> { postFirebaseRepository.uploadImage(testBitmap) }
  }
}
