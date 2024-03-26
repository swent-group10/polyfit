package com.github.se.polyfit

import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealsFirebaseConnection
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MealsFirebaseConnectionTest {
    @Test
    fun addMealActualFirebase() {
        val database = MealsFirebaseConnection()
        val meal = Meal(
            "1",
            MealOccasion.DINNER,
            "eggs",
            102.2,
            12301.3,
            1234.9,
            12303.0
        )
        val result = database.addMeal("1", meal)
        runBlocking {
            assert(result.isSuccessful)
        }

    }

    @Test
    fun addMealTest() {
        val mockFirestore = mockk<FirebaseFirestore>()
        val mockDocumentSnapshot = mockk<DocumentSnapshot>()
        val meal = Meal(
            "1",
            MealOccasion.DINNER,
            "eggs",
            102.2,
            12301.3,
            1234.9,
            12303.0
        )
        val mealMap = Meal.serializeMeal(meal)
        every { mockDocumentSnapshot.data } returns mealMap
        every {
            mockFirestore.collection("users").document("1").collection("meals").document(meal.uid)
                .set(mealMap)
        } returns Tasks.forResult(null)
        val database = MealsFirebaseConnection(mockFirestore)
        val result = database.addMeal("1", meal)
        assert(result.isSuccessful)
    }

    @Test
    fun getAllMealsTest() {
        val mockFirestore = mockk<FirebaseFirestore>()
        val mockQuerySnapshot = mockk<QuerySnapshot>()
        val mockDocumentSnapshot = mockk<DocumentSnapshot>()
        val meal = Meal(
            "1",
            MealOccasion.DINNER,
            "eggs",
            102.2,
            12301.3,
            1234.9,
            12303.0
        )
        val mealMap = Meal.serializeMeal(meal)
        every { mockDocumentSnapshot.data } returns mealMap
        every { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)
        every {
            mockFirestore.collection("users").document("1").collection("meals").get()
        } returns Tasks.forResult(mockQuerySnapshot)
        val database = MealsFirebaseConnection(mockFirestore)
        val result = database.getAllMeals("1").result
        assert(result?.size == 1)
        assert(result?.get(0) == meal)
    }

    @Test
    fun updateMealTest() {
        val mockFirestore = mockk<FirebaseFirestore>()
        val mockDocumentSnapshot = mockk<DocumentSnapshot>()
        val meal = Meal(
            "1",
            MealOccasion.DINNER,
            "eggs",
            102.2,
            12301.3,
            1234.9,
            12303.0
        )
        val mealMap = Meal.serializeMeal(meal)
        every { mockDocumentSnapshot.data } returns mealMap
        every {
            mockFirestore.collection("users").document("1").collection("meals").document(meal.uid)
                .set(mealMap)
        } returns Tasks.forResult(null)
        val database = MealsFirebaseConnection(mockFirestore)
        val result = database.updateMeal("1", meal)
        assert(result.isSuccessful)
    }
}