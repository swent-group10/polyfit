/*
package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.post.Post
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before

class PostFirebaseRepositoryFetchPosts {

  private lateinit var postFirebaseRepository: PostFirebaseRepository
  private val mockDb: FirebaseFirestore = mockk(relaxed = true)
  private val mockCollection: CollectionReference = mockk(relaxed = true)
  private val mockQuerySnapshot: QuerySnapshot = mockk(relaxed = true)
  private val mockDocumentSnapshot: DocumentSnapshot = mockk(relaxed = true)

  @Before
  fun setUp() {
    Dispatchers.setMain(Dispatchers.Unconfined)
    postFirebaseRepository = PostFirebaseRepository(db = mockDb)
    coEvery { mockDb.collection(any()) } returns mockCollection
    coEvery { mockCollection.whereIn(any<FieldPath>(), any()).get() } returns
        Tasks.forResult(mockQuerySnapshot)
    coEvery { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)
    coEvery { mockDocumentSnapshot.data } returns
        mapOf(
            "userId" to "userId",
            "description" to "description",
            "location" to
                mapOf("longitude" to 0.0, "latitude" to 0.0, "altitude" to 10.0, "name" to "EPFL"),
            "meal" to Meal.default().serialize(),
            "createdAt" to
                mapOf(
                    "year" to 2021.toLong(),
                    "monthValue" to 10.toLong(),
                    "dayOfMonth" to 10.toLong()))
  }

  @Test
  fun fetchPostsAndImagesReturnsListOfPostsOnSuccess() = runTest {
    val keys =
        listOf(
            "key1",
            "key2",
            "key3",
            "key4",
            "key5",
            "key6",
            "key7",
            "key8",
            "key9",
            "key10",
            "key11",
            "key12",
            "key13",
            "key14",
            "key15",
            "key16")
    var result: List<Post> = listOf()
    postFirebaseRepository.fetchPostsAndImages(keys, postCollection = mockCollection) { posts ->
      result = posts
    }

    coVerify {
      mockCollection.whereIn(
          FieldPath.documentId(),
          listOf("key1", "key2", "key3", "key4", "key5", "key6", "key7", "key8", "key9", "key10"))
    }
    coVerify {
      mockCollection.whereIn(
          FieldPath.documentId(), listOf("key11", "key12", "key13", "key14", "key15", "key16"))
    }
  }

  @Test
  fun fetchPostsAndImagesReturnsEmptyListWhenNoPosts() = runTest {
    val keys = emptyList<String>()
    var result: List<Post> = listOf()
    postFirebaseRepository.fetchPostsAndImages(keys) { posts -> result = posts }

    coVerify(exactly = 0) { mockCollection.whereIn(any<FieldPath>(), any()).get() }
    assertEquals(0, result?.size)
  }
}
*/
