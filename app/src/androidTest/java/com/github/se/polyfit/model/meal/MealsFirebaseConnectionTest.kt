package com.github.se.polyfit.model.meal

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MealsFirebaseConnectionTest {

    @Test
    fun getAllMeals_returnsCorrectMeals() {
        // Create a mock FirebaseFirestore instance
        val mockFirestore = mockk<FirebaseFirestore>()

        // Create a mock QuerySnapshot
        val mockQuerySnapshot = mockk<QuerySnapshot>()

        // Create a mock DocumentSnapshot
        val mockDocumentSnapshot = mockk<DocumentSnapshot>()

        // Create a Meal object
        val meal = Meal(
            "1",
            MealOccasion.DINNER,
            "eggs",
            102.2,
            12301.3,
            1234.9,
            12303.0
        )

        // Convert the Meal object to a Map
        val mealMap = Meal.serializeMeal(meal)

        // When `document.data` is called on the mock DocumentSnapshot, return the map representing the Meal
        every { mockDocumentSnapshot.data } returns mealMap

        // When `documents` is called on the mock QuerySnapshot, return a list containing the mock DocumentSnapshot
        every { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)

        // When `get` is called on the Firestore instance, return a completed Task with the mock QuerySnapshot
        every {
            mockFirestore.collection("users").document("1").collection("meals").get()
        } returns Tasks.forResult(mockQuerySnapshot)

        // Create a MealsFirebaseConnection with the mock FirebaseFirestore instance
        val database = MealsFirebaseConnection(mockFirestore)

        // Call getAllMeals and assert that it returns a list containing the correct Meal
        val result = database.getAllMeals("1").result
        assert(result?.size == 1)
        assert(result?.get(0) == meal)
    }
}