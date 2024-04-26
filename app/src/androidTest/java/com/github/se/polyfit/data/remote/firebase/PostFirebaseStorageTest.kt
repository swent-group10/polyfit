package com.github.se.polyfit.data.remote.firebase

import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import java.time.LocalDate
import kotlin.test.Test

class PostFirebaseStorageTest {
    private lateinit var postFirebaseRepository: PostFirebaseRepository
    private val mockDb: FirebaseFirestore = mockk()
    private val mockPictureDb: FirebaseStorage = mockk()
    private val mockCollectionRef = mockk<CollectionReference>()


    @Before
    fun setUp() {
        every { mockDb.collection("posts") } returns mockCollectionRef
        postFirebaseRepository = PostFirebaseRepository(mockDb, mockPictureDb)
    }

    @Test
    fun storePostReturnsVoidOnSuccess() = runTest {
        val steam =
            InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")

        val testPost = Post(
            "userId",
            "description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now(),
            listOf(BitmapFactory.decodeStream(steam))

        )


        val mockDocRef = mockk<DocumentReference>()
        val task = Tasks.forResult(mockDocRef)

        coEvery { mockCollectionRef.add(testPost.serialize()) } returns task
//        coEvery { task.result } returns DocumentReference

        val result = postFirebaseRepository.storePost(testPost).await()

        Assert.assertEquals(mockDocRef, result)
    }
}
