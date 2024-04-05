import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MealFirebaseRepositoryTest {
    private val meal = Meal(
        mealID = 1,
        name = "Test Meal",
        occasion = MealOccasion.BREAKFAST,
        mealTemp = 20.0,
        nutritionalInformation = NutritionalInformation(mutableListOf()),
        ingredients = mutableListOf(),
        firebasaeId = "1"
    )
    private val meal2 = Meal(
        mealID = 2,
        name = "Test Meal 2",
        occasion = MealOccasion.BREAKFAST,
        mealTemp = 20.0,
        nutritionalInformation = NutritionalInformation(mutableListOf()),
        ingredients = mutableListOf(),
        firebasaeId = "2"
    )

    private var db: MealFirebaseRepository = MealFirebaseRepository("0") // using user id "0"

    private fun runBlockingTest(block: suspend () -> Unit) {
        runBlocking { block() }
    }

    @Test
    fun test1StoreMealSuccessfully() {
        runBlockingTest {
            val result = db.storeMeal(meal).await()

            assert(result.id.isNotEmpty())
            assert(meal.firebasaeId.isNotEmpty())
            assert(result.id == meal.firebasaeId)
        }
    }

    @Test
    fun test2GetMealSuccessfully() {
        runBlockingTest {
            // Call the method under test
            assert(meal.firebasaeId.isNotEmpty())
            val result = db.getMeal(meal.firebasaeId)

            // Assert that the result is as expected
            result.continueWith {
                assertEquals(it.isSuccessful, true)
                assertEquals(it.result, meal)
                // assertNull(it.result) // remove or modify this line
            }
        }
    }

    @Test
    fun test3GetAllMealsSuccessfully() {
        runBlockingTest {

            db.storeMeal(meal2).await()

            val result = db.getAllMeals().await()

            assert(result.isNotEmpty())
            assert(result.contains(meal))
            assert(result.contains(meal2))
            assertEquals(listOf(meal, meal2), result)

        }
    }

    @Test
    fun test4DeleteMealSuccessfully() {
        runBlockingTest {

            db.deleteMeal(meal.firebasaeId).await()

            db.getMeal(meal.firebasaeId).continueWith {
                assertNull(it.result)
            }

            db.deleteMeal(meal2.firebasaeId).await()

            db.getMeal(meal2.firebasaeId).continueWith {
                assertNull(it.result)
            }
        }
    }
}