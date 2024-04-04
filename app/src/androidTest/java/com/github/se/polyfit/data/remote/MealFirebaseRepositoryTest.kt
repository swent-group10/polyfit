import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Task
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

        every { firestore.collection(any()) } returns collectionReference
    }

    @Test
    fun storeMealSuccessfully() {
        val task = mockk<Task<DocumentReference>>()
        every {
            firestore.collection(any()).document(any()).collection(any()).add(any())
        } returns task

        val result = db.storeMeal(meal)

        assertEquals(task, result)
    }

    @Test
    fun getMealSuccessfully() {
        val task = mockk<Task<DocumentSnapshot>>()
        val documentSnapshot = mockk<DocumentSnapshot>()
        every {
            firestore.collection(any()).document(any()).collection(any()).document(any()).get()
        } returns task
        every { documentSnapshot.toObject(Meal::class.java) } returns meal

        val result = db.getMeal("0")

        val mealTask = task.continueWith { task ->
            val snapshot = task.result
            snapshot?.toObject(Meal::class.java)
        }

        assertEquals(mealTask, result)
    }

    @Test
    fun getAllMealsSuccessfully() {
        val task = mockk<Task<QuerySnapshot>>()
        every { firestore.collection(any()).document(any()).collection(any()).get() } returns task

        val result = db.getAllMeals()

        assertEquals(task, result)
    }

    @Test(expected = Exception::class)
    fun getMealThrowsExceptionWhenFailed() {
        every {
            firestore.collection(any()).document(any()).collection(any()).document(any()).get()
        } throws Exception()

        db.getMeal("0")
    }

    @Test(expected = Exception::class)
    fun getAllMealsThrowsExceptionWhenFailed() {
        every {
            firestore.collection(any()).document(any()).collection(any()).get()
        } throws Exception()

        db.getAllMeals()
    }
}