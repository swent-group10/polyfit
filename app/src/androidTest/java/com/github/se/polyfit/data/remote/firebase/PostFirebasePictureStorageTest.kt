package com.github.se.polyfit.data.remote.firebase

// Alphabetically ordered imports
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.io.ByteArrayInputStream
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import org.junit.Before

class PostFirebasePictureStorageTest {
  // Mock objects
  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private val mockDb: FirebaseFirestore = mockk(relaxed = true)
  private val mockPictureDb: FirebaseStorage = mockk(relaxed = true)
  private val mockCollectionRef = mockk<CollectionReference>(relaxed = true)
  private lateinit var testPost: Post

  @Before
  fun setUp() {
    // Mock the collection method of the Firestore database
    every { mockDb.collection("posts") } returns mockCollectionRef
    postFirebaseRepository = PostFirebaseRepository(mockDb, mockPictureDb)

    // Open the image file
    val steam = InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")
    // Create a test post
    testPost =
        Post(
            "userId",
            "description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now(),
            listOf(BitmapFactory.decodeStream(steam)))
  }

  @Test
  fun failureToStoreImage() {
    // Mock the document reference and the task
    val mockDocRef = mockk<DocumentReference>()
    val mockTask: Task<DocumentReference> = Tasks.forResult(mockDocRef)

    // Mock the add method of the collection reference
    coEvery { mockCollectionRef.add(testPost.serialize()) } returns mockTask
    coEvery { mockDocRef.path } returns "path"

    val mockRefSource = mockk<StorageReference>(relaxed = true)
    coEvery { mockPictureDb.getReference(any()) } returns mockRefSource

    val mockUploadTask = mockk<UploadTask>(relaxUnitFun = true)
    coEvery { mockRefSource.putStream(any<ByteArrayInputStream>()) } returns mockUploadTask
    // Call the method under test and check the result

    val exception =
        assertFailsWith<Exception> { runBlocking { postFirebaseRepository.storePost(testPost) } }
    assert(exception.message!!.contains("Error uploading images"))
  }
}
