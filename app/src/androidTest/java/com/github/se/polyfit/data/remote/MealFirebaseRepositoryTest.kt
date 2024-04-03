import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MealFirebaseRepositoryTest {

    private lateinit var repository: MealFirebaseRepository
    private lateinit var firestore: FirebaseFirestore
    private lateinit var mealCollection: CollectionReference

    @Before
    fun setup() {
        firestore = mockk()
        mealCollection = mockk()

        // Mock the Firestore collection path
        coEvery {
            firestore.collection("users").document("testUserId").collection("meals")
        } returns mealCollection

        repository = MealFirebaseRepository("testUserId", firestore)
    }

    @Test
    fun storeMealSuccessfully() = runTest {
        val meal = Meal(MealOccasion.DINNER, "Chicken Salad", 500, 1.5, mockk(relaxed = true))
        val documentReference: DocumentReference = mockk()

        // Mock adding a document to Firestore
        coEvery { mealCollection.add(any()) } returns Tasks.forResult(documentReference)

        val result = repository.storeMeal(meal).await()
        assertEquals(documentReference, result)
    }

    @Test
    fun getMealSuccessfully() = runTest {
        val mealId = "mealId"
        val documentSnapshot: DocumentSnapshot = mockk()
        val meal: Meal = mockk(relaxed = true)
        val documentReference: DocumentReference = mockk()

        // Mock fetching a document by ID
        coEvery { mealCollection.document(mealId) } returns documentReference
        coEvery { documentReference.get() } returns Tasks.forResult(documentSnapshot)
        coEvery { documentSnapshot.toObject(Meal::class.java) } returns meal

        val result = repository.getMeal(mealId).await()
        assertEquals(meal, result)
    }

    @Test
    fun getAllMealsSuccessfully() = runTest {
        val documentSnapshot1: DocumentSnapshot = mockk()
        val documentSnapshot2: DocumentSnapshot = mockk()
        val meal1: Meal = mockk(relaxed = true)
        val meal2: Meal = mockk(relaxed = true)
        val querySnapshot: QuerySnapshot = mockk()

        // Mock fetching all documents from a collection
        coEvery { mealCollection.get() } returns Tasks.forResult(querySnapshot)
        coEvery { querySnapshot.documents } returns listOf(documentSnapshot1, documentSnapshot2)
        coEvery { documentSnapshot1.toObject(Meal::class.java) } returns meal1
        coEvery { documentSnapshot2.toObject(Meal::class.java) } returns meal2

        val result = repository.getAllMeals().await()
        assertEquals(listOf(meal1, meal2), result)
    }
}
