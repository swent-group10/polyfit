package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.recipe.Recipe
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before

class RecipeFirebaseRepositoryTest {
  private lateinit var recipeFirebaseRepository: RecipeFirebaseRepository
  private val mockFirebase: FirebaseFirestore = mockk()
  private val mockCollection: CollectionReference = mockk()
  private val mockDocumentRef: DocumentReference = mockk()
  private val mockkRecipeCollection: CollectionReference = mockk()

  @Before
  fun setup() {

    every { mockFirebase.collection("users") } returns mockCollection
    every { mockCollection.document(any()) } returns mockDocumentRef
    every { mockDocumentRef.collection("recipes") } returns mockkRecipeCollection

    recipeFirebaseRepository = RecipeFirebaseRepository(userId = "10", db = mockFirebase)
  }

  @Test
  fun storeRecipeSuccess() = runTest {
    val successfulTask = Tasks.forResult<Void>(mockk())
    every { mockkRecipeCollection.document() } returns mockDocumentRef
    every { mockDocumentRef.set(any()) } returns successfulTask
    coEvery { mockDocumentRef.id } returns "someid"
    val recipe = Recipe.default()
    assert(recipe.firebaseId.isEmpty())
    recipeFirebaseRepository.storeRecipe(recipe).await()

    // Trigger the addOnSuccessListener block
    successfulTask.result

    assertEquals("someid", recipe.firebaseId)
  }

  @Test
  fun storeRecipeSuccessWithFirebaseIdAlreadySet() = runTest {
    val successfulTask = Tasks.forResult<Void>(mockk())
    every { mockkRecipeCollection.document("testid") } returns mockDocumentRef
    every { mockDocumentRef.set(any()) } returns successfulTask
    coEvery { mockDocumentRef.id } returns "someid"
    val recipe = Recipe.default().apply { firebaseId = "testid" }
    assertFalse(recipe.firebaseId.isEmpty())
    recipeFirebaseRepository.storeRecipe(recipe).await()

    // Trigger the addOnSuccessListener block
    successfulTask.result

    assertEquals("testid", recipe.firebaseId)
  }

  @Test
  fun storeRecipeFailure() = runTest {
    val successfulTask = Tasks.forException<Void>(Exception("Error storing the task"))
    every { mockkRecipeCollection.document() } returns mockDocumentRef
    every { mockDocumentRef.set(any()) } returns successfulTask
    coEvery { mockDocumentRef.id } returns "someid"
    val recipe = Recipe.default()
    assert(recipe.firebaseId.isEmpty())

    assertFailsWith<Exception> { recipeFirebaseRepository.storeRecipe(recipe).await() }
  }

  @Test
  fun emptyFirebaseIdFailure() = runTest {
    assertFailsWith<IllegalArgumentException> { recipeFirebaseRepository.getRecipe("") }
  }

  @Test
  fun getRecipeSuccess() = runTest {
    val firebaseId = "SomeFunkyId"
    val mockDocumentSnapshot: DocumentSnapshot = mockk()
    every { mockkRecipeCollection.document(firebaseId).get() } returns
        Tasks.forResult(mockDocumentSnapshot)

    every { mockDocumentSnapshot.data } returns Recipe.serialize(Recipe.default())

    val recipe = recipeFirebaseRepository.getRecipe(firebaseId).await()

    assertEquals(Recipe.default(), recipe)
  }

  @Test
  fun getRecipeFailure() = runTest {
    val firebaseId = "SomeFunkyId"
    every { mockkRecipeCollection.document(firebaseId).get() } returns
        Tasks.forException(Exception("Some exception"))

    assertFails { recipeFirebaseRepository.getRecipe(firebaseId).await() }
  }
}
