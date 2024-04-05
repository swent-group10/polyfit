import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MealRepositoryTest {

  private lateinit var mealRepository: MealRepository
  private lateinit var mealFirebaseRepository: MealFirebaseRepository

  @Before
  fun setup() {
    mealFirebaseRepository = mockk()
    mealRepository = MealRepository(mealFirebaseRepository)
  }

  @Test
  fun storeMeal_successfullyStoresMeal() = runTest {
    val meal =
        Meal(
            MealOccasion.DINNER,
            "name",
            1,
            12.0,
            NutritionalInformation(mutableListOf()),
            mutableListOf())
    val documentReference = mockk<DocumentReference>()
    coEvery { mealFirebaseRepository.storeMeal(meal) } returns Tasks.forResult(documentReference)

    mealRepository.storeMeal(meal)
  }

  @Test
  fun getMeal_returnsExpectedMeal() = runTest {
    val expectedMeal =
        Meal(
            MealOccasion.DINNER,
            "name",
            1,
            12.0,
            NutritionalInformation(mutableListOf()),
            mutableListOf())

    coEvery { mealFirebaseRepository.getMeal("id") } returns Tasks.forResult(expectedMeal)

    val actualMeal = mealRepository.getMeal("id")

    assertEquals(expectedMeal, actualMeal)
  }

  @Test
  fun getAllMeals_returnsExpectedMeals() = runTest {
    val expectedMeals =
        listOf(
            Meal(
                MealOccasion.DINNER,
                "name1",
                1,
                1.2,
                NutritionalInformation(mutableListOf()),
            ),
            Meal(MealOccasion.DINNER, "name2", 2, 1.3, NutritionalInformation(mutableListOf())))
    coEvery { mealFirebaseRepository.getAllMeals() } returns Tasks.forResult(expectedMeals)

    val actualMeals = mealRepository.getAllMeals()

    assertEquals(expectedMeals, actualMeals)
  }

  @Test
  fun deleteMeal_successfullyDeletesMeal() = runTest {
    coEvery { mealFirebaseRepository.deleteMeal("id") } returns Tasks.forResult(null)

    mealRepository.deleteMeal("id")
  }
}
