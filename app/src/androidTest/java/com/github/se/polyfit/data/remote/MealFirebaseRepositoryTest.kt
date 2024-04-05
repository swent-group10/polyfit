import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MealFirebaseRepositoryTest {

    private lateinit var db: MealFirebaseRepository
    private lateinit var firestore: FirebaseFirestore
    private lateinit var meal: Meal
    private lateinit var task: Task<QuerySnapshot>

    @Before
    fun setUp() {
        firestore = mockk(relaxed = true)
        val ingredient = Ingredient(
            "Test Ingredient", 1, NutritionalInformation(
                mutableListOf()
            )
        )// Create a real instance of Ingredient
        val ingredients = mutableListOf(ingredient)
        meal = mockk<Meal>(relaxed = true)
        every { meal.mealID } returns 1
        every { meal.occasion } returns MealOccasion.BREAKFAST // replace with the appropriate MealOccasion
        every { meal.name } returns "Test Meal"
        every { meal.mealTemp } returns 20.0
        every { meal.nutritionalInformation } returns mockk(relaxed = true)
//        every { meal.ingredients } returns ingredients
        db = MealFirebaseRepository("test", firestore)

        val documentReference = mockk<DocumentReference>(relaxed = true)
        val collectionReference = mockk<CollectionReference> {
            every { document(any()) } returns documentReference
        }

        task = mockk<Task<QuerySnapshot>>()
        every {
            firestore.collection(any()).document(any()).collection(any()).get()
        } returns task

        every { firestore.collection(any()) } returns collectionReference
    }

    @Test
    fun storeMealSuccessfully() {
        val result = db.storeMeal(meal)
        result.continueWith {
            assertEquals(it.isSuccessful, true)
        }
    }


    // Then in your test, you can mock the TaskWrapper class instead of the Task class
    @Test
    fun getMealSuccessfully() {
        // Mock the DocumentSnapshot
        val documentSnapshot = mockk<DocumentSnapshot>()
        every { documentSnapshot.toObject(Meal::class.java) } returns meal

        // Mock the Task
        val task = mockk<Task<DocumentSnapshot>>()
        every { task.isSuccessful } returns true
        every { task.result } returns documentSnapshot

        // Mock the Firestore call
        every {
            firestore.collection(any()).document(any()).collection(any()).document(any()).get()
        } returns task

        // Call the method under test
        val result = db.getMeal("0")

        // Assert that the result is as expected
        result.continueWith {
            assertEquals(it.isSuccessful, true)
            assertEquals(it.result, meal)
        }
    }

    @Test
    fun getAllMealsSuccessfully() {
        val querySnapshot = mockk<QuerySnapshot>()
        every { querySnapshot.documents } returns mutableListOf(mockk<DocumentSnapshot> {
            every { toObject(Meal::class.java) } returns meal
        })

        task = mockk<Task<QuerySnapshot>> {
            every { isSuccessful } returns true
            every { result } returns querySnapshot
        }
        every {
            firestore.collection(any()).document(any()).collection(any()).get()
        } returns task

        val result = db.getAllMeals()

        result.continueWith {
            assertEquals(it.isSuccessful, true)
//            assertEquals(it.result?.documents?.map { it.toObject(Meal::class.java) }, listOf(meal))
        }
    }

    @Test
    fun getMealThrowsExceptionWhenFailed() {
        every {
            firestore.collection(any()).document(any()).collection(any()).document(any()).get()
        } returns Tasks.forException(Exception("Failed to fetch meals"))

        val task = db.getMeal("0")

        task.continueWith {
            assertEquals(it.isSuccessful, false)
            assertEquals(it.exception?.message, "Failed to fetch meals")
        }

    }

}