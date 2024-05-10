package com.github.se.polyfit.data.remote.firebase

import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MealFirebaseRepositoryTest {

  private lateinit var mockDb: FirebaseFirestore
  private lateinit var mockDocumentReference: DocumentReference
  private lateinit var mockDocumentSnapshot: DocumentSnapshot
  private lateinit var mockQuerySnapshot: QuerySnapshot
  private lateinit var mealFirebaseRepository: MealFirebaseRepository
  private lateinit var mockCollection: CollectionReference
  private lateinit var meal: Meal
  private lateinit var mealNoId: Meal

  @Before
  fun setup() {
    mockDb = mockk()
    mockDocumentReference = mockk()
    mockDocumentSnapshot = mockk()
    mockQuerySnapshot = mockk()

    mockCollection = mockk<CollectionReference>()
    every { mockDb.collection("users") } returns mockCollection
    every { mockCollection.document("0") } returns mockDocumentReference
    every { mockDocumentReference.collection("meals") } returns mockCollection

    mealFirebaseRepository = MealFirebaseRepository("0", mockDb)

    meal =
        Meal(
            name = "Test Meal",
            occasion = MealOccasion.BREAKFAST,
            mealTemp = 20.0,
            ingredients = mutableListOf(),
            id = "1")
    mealNoId =
        Meal(
            name = "Test Meal",
            occasion = MealOccasion.BREAKFAST,
            mealTemp = 20.0,
            ingredients = mutableListOf(),
            id = "")
  }

  @Test
  fun storeMealSuccessfully() = runBlocking {
    every { mockCollection.document(any()) } returns mockDocumentReference
    every { mockDocumentReference.set(any()) } returns Tasks.forResult(null)
    val result = mealFirebaseRepository.storeMeal(meal).await()
    assertEquals(mockDocumentReference, result)
  }

  @Test
  fun storeMealFailure(): Unit = runBlocking {
    every { mockCollection.document(any()) } returns mockDocumentReference
    every { mockDocumentReference.set(any()) } returns
        Tasks.forException(Exception("Failed to store meal"))
    assertFails { runBlocking { mealFirebaseRepository.storeMeal(meal).await() } }
  }

  @Test
  fun getMealSuccessfully() = runBlocking {
    every { mockCollection.document(meal.id) } returns mockDocumentReference
    every { mockDocumentReference.get() } returns Tasks.forResult(mockDocumentSnapshot)
    every { mockDocumentSnapshot.data } returns Meal.serialize(meal)

    val result = mealFirebaseRepository.getMeal(meal.id).await()

    assertEquals(meal, result)
    assertEquals(meal.occasion, result!!.occasion)
  }

  @Test
  fun getMealSuccessfullyFailedSerialization() = runTest {
    val mealData = Meal.serialize(meal).toMutableMap()
    // change occasion to a wrong value
    mealData["occasion"] = "WRONG"

    every { mockCollection.document(meal.id) } returns mockDocumentReference
    every { mockDocumentReference.get() } returns Tasks.forResult(mockDocumentSnapshot)
    every { mockDocumentSnapshot.data } returns mealData

    assertFails { runBlocking { mealFirebaseRepository.getMeal(meal.id).await() } }
  }

  @Test
  fun getMealSuccessfullyFailedTask() = runTest {
    every { mockCollection.document(meal.id) } returns mockDocumentReference
    every { mockDocumentReference.get() } returns
        Tasks.forException(Exception("Failed to fetch meal"))

    assertFails { runBlocking { mealFirebaseRepository.getMeal(meal.id).await() } }
  }

  @Test
  fun getAllMealsSuccessfully() = runBlocking {
    every { mockCollection.get() } returns Tasks.forResult(mockQuerySnapshot)
    every { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)
    every { mockDocumentSnapshot.data } returns Meal.serialize(meal)
    every { mockDocumentSnapshot.id } returns meal.id

    val result = mealFirebaseRepository.getAllMeals().await()

    assertEquals(listOf(meal), result)
    assert(result.first()!!.mealTemp == meal.mealTemp)
    assert(result.first()!!.name == meal.name)
  }

  @Test
  fun getAllMealSuccessfullyFailedSerialization() = runTest {
    val mealData = Meal.serialize(meal).toMutableMap()
    // change occasion to a wrong value
    mealData["occasion"] = "WRONG"

    every { mockCollection.document(meal.id) } returns mockDocumentReference
    every { mockCollection.get() } returns Tasks.forResult(mockQuerySnapshot)
    every { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)
    every { mockDocumentSnapshot.data } returns mealData

    assertFails { runBlocking { mealFirebaseRepository.getAllMeals().await() } }
  }

  @Test
  fun getAllMealsTaskFaillure(): Unit = runBlocking {
    every { mockCollection.get() } returns Tasks.forException(Exception("Failed to fetch meals"))
    every { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)
    every { mockDocumentSnapshot.data } returns Meal.serialize(meal)
    every { mockDocumentSnapshot.id } returns meal.id

    assertFails { runBlocking { mealFirebaseRepository.getAllMeals().await() } }
  }

  @Test
  fun deleteMealSuccessfully() = runBlocking {
    every { mockCollection.document(meal.id) } returns mockDocumentReference
    every { mockDocumentReference.delete() } returns Tasks.forResult(null)

    val res = mealFirebaseRepository.deleteMeal(meal.id).await()
    assert(res == Unit)
  }

  @Test
  fun deleteMealTaskFaillure(): Unit = runBlocking {
    every { mockCollection.document(meal.id) } returns mockDocumentReference
    every { mockDocumentReference.delete() } returns
        Tasks.forException(Exception("Failed to delete meal"))

    assertFails { mealFirebaseRepository.deleteMeal(meal.id).await() }
  }
}
