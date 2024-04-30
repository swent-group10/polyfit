package com.github.se.polyfit.data.remote.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import java.io.File
import java.io.FileOutputStream
import kotlin.test.assertEquals

class PushTest {

  @Test
  fun pushTest() = runTest {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.food1)
    val listOfImages = listOf(bitmap)
    val post =
        Post(
            "C'est moi",
            "les images c po facil",
            com.github.se.polyfit.model.post.Location(12.0, 4.0, 10.0, "EPFL"),
            Meal.default(),
            java.time.LocalDate.now(),
            listOfImages)
    val ref = PostFirebaseRepository().storePost(post)

  }

    @Test
    fun fetchTest() = runTest {

        val keys: List<String> = listOf("I32OPUTFfUiKytxM5v21")
        val ref = PostFirebaseRepository().fetchPostsAndImages(keys) { a -> Log.d("TEST", a.toString()) }
    }

    @Test
    fun fetchImage() = runTest {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword("benjo.burki@gmail.com", "Konforever66")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User was created
                    val user = auth.currentUser
                    println("User created: ${user?.email}")
                } else {
                    // User creation failed
                    println("User creation failed: ${task.exception?.message}")
                }
            }

        val ref = PostFirebaseRepository().fetchImagesForPost("I32OPUTFfUiKytxM5v21")
    }

    @Test
    fun fetchImagesForPostReturnsExpectedResult() = runBlocking {
        val mockStorage: FirebaseStorage = mockk(relaxed = true)
        val mockStorageRef: StorageReference = mockk(relaxed = true)
        val mockListResult: ListResult = mockk(relaxed = true)
        val mockStorageItem: StorageReference = mockk(relaxed = true)

        val expectedBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        // Mock the behavior of Firebase Storage
        every { mockStorage.getReference("posts/testPostKey") } returns mockStorageRef
        coEvery { mockStorageRef.listAll() } returns Tasks.forResult(mockListResult)
        every { mockListResult.items } returns listOf(mockStorageItem)
        coEvery { mockStorageItem.getFile(any<File>()) } answers {
            val file = firstArg<File>()
            FileOutputStream(file).use { out ->
                expectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            mockk(relaxed = true)
        }

        // Create an instance of PostFirebaseRepository with the mocked Firebase Storage
        val postFirebaseRepository = PostFirebaseRepository(pictureDb = mockStorage)

        // Call fetchImagesForPost and verify the result
        val result = postFirebaseRepository.fetchImagesForPost("testPostKey")
        assertEquals(listOf(expectedBitmap), result)
    }
}
