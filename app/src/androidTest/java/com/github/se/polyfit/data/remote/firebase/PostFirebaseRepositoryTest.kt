package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Location
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class PostFirebaseRepositoryTest {

    private lateinit var postFirebaseRepository: PostFirebaseRepository
    private val mockDb: FirebaseFirestore = mockk()
    private val mockCollectionRef = mockk<CollectionReference>()

    @Before
    fun setUp() {

        // Set up the mock to return a mock CollectionReference when collection is called
        every { mockDb.collection("posts") } returns mockCollectionRef
        postFirebaseRepository = PostFirebaseRepository(mockDb)
    }


    @Test
    fun storePostReturnsDocumentReferenceOnSuccess() = runTest {
        val post = Post(
            "userId",
            "description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now()
        )
        val mockDocRef = mockk<DocumentReference>()
        val task = Tasks.forResult(mockDocRef)


        coEvery { mockCollectionRef.add(post.serialize()) } returns task

        val result = postFirebaseRepository.storePost(post).await()

        assertEquals(mockDocRef, result)
    }

    @Test
    fun testStoreFaillure() {
        val failledTask = Tasks.forException<Exception>(Exception("errorFetchingdata"))

        coEvery { mockCollectionRef.add(any()) } returns failledTask
    }
}