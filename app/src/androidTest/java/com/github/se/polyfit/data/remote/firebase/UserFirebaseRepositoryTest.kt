package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.data.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before

class UserFirebaseRepositoryTest {
  private lateinit var userFirebaseRepository: UserFirebaseRepository
  private val mockFirebase: FirebaseFirestore = mockk()
  private val mockCollection: CollectionReference = mockk()
  private val mockDocumentRef: DocumentReference = mockk()
  private val successfulTask = Tasks.forResult<Void>(mockk())

  @Before
  fun setup() {

    every { mockFirebase.collection("users") } returns mockCollection
    every { mockCollection.document(any()) } returns mockDocumentRef

    userFirebaseRepository = UserFirebaseRepository(db = mockFirebase)
  }

  @Test
  fun storeUserSuccess() {
    val user =
        User(
            id = "1",
            displayName = "John Doe",
            familyName = "Doe",
            givenName = "John",
            email = "john@doe.com")

    every { mockCollection.document(user.id) } returns mockDocumentRef
    every { mockDocumentRef.set(any()) } returns successfulTask

    userFirebaseRepository.storeUser(user).continueWith { task -> assert(task.isSuccessful) }
  }

  @Test
  fun storeUser_failWithIncompleteData() {
    val user = User(id = "1", email = "john@doe.com")

    every { mockCollection.document(user.id) } returns mockDocumentRef
    every { mockDocumentRef.set(any()) } returns successfulTask

    assertFailsWith<Exception> {
      userFirebaseRepository.storeUser(user).continueWith { task -> assert(task.isSuccessful) }
    }
  }

  @Test
  fun getUserSuccess() = runTest {
    val user =
        User(
            id = "1",
            displayName = "John Doe",
            familyName = "Doe",
            givenName = "John",
            email = "john@doe.com")

    val mockDocumentSnapshot: DocumentSnapshot = mockk()
    every { mockCollection.document(user.id).get() } returns Tasks.forResult(mockDocumentSnapshot)
    every { mockDocumentSnapshot.data } returns user.serialize()

    val result = userFirebaseRepository.getUser(user.id).await()

    assertEquals(result, user)
  }

  @Test
  fun getUserFail() = runTest {
    val user =
        User(
            id = "1",
            displayName = "John Doe",
            familyName = "Doe",
            givenName = "John",
            email = "john@doe.com")

    every { mockCollection.document(user.id).get() } returns Tasks.forException(mockk())

    assertFailsWith<Exception> { userFirebaseRepository.getUser(user.id).await() }
  }
}
