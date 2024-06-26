import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.Before

class MealRepositoryTest {

  private lateinit var mealRepository: MealRepository
  private lateinit var mealFirebaseRepository: MealFirebaseRepository
  private lateinit var mealDao: MealDao
  private lateinit var checkConnectivity: MealRepository.ConnectivityChecker
  private lateinit var user: User

  @Before
  fun setup() {
    mealFirebaseRepository = mockk()
    mealDao = mockk()
    checkConnectivity = mockk()
    user = User.testUser()
    val testContext: Context = ApplicationProvider.getApplicationContext()

    mealRepository =
        MealRepository(
            mealFirebaseRepository = mealFirebaseRepository,
            mealDao = mealDao,
            checkConnectivity = checkConnectivity,
            context = testContext,
            user = user)
  }

  @Test
  fun storeMeal_whenConnectionAvailableAndDataNotOutdated_storesMealInFirebaseAndLocalDb() =
      runTest {
        val meal = Meal(MealOccasion.DINNER, "name", "1", "testId", 12.0, mutableListOf())
        val documentReference = mockk<DocumentReference>()
        coEvery { mealFirebaseRepository.storeMeal(meal) } returns
            Tasks.forResult(documentReference)
        coEvery { mealDao.insert(any<Meal>()) } returns "109"
        coEvery { checkConnectivity.checkConnection() } returns true

        val result = mealRepository.storeMeal(meal)

        assertEquals(documentReference, result)
      }

  @Test
  fun storeMeal_whenConnectionNotAvailable_storesMealInLocalDbOnly() = runTest {
    val meal = Meal(MealOccasion.DINNER, "name", "1", "testId", 12.0, mutableListOf())
    coEvery { mealDao.insert(any<Meal>()) } returns "109"
    coEvery { checkConnectivity.checkConnection() } returns false

    val result = mealRepository.storeMeal(meal)

    assertNull(result)
  }

  @Test
  fun getMeal() = runTest {
    val default = Meal.default()
    every { mealDao.getMealById(any()) } returns default

    val result = mealRepository.getMealById("1")

    assertEquals(default, result)
  }

  @Test
  fun getAllMeals() = runTest {
    val meal1 = Meal.default()
    val meal2 = Meal.default()
    every { mealDao.getAllMeals("testId") } returns listOf(meal1, meal2)

    val result = mealRepository.getAllMeals()

    assertEquals(listOf(meal1, meal2), result)
  }

  @Test
  fun deleteMeal_whenConnectionAvailableAndDataNotOutdated_deletesMealFromFirebaseAndLocalDb() =
      runTest {
        coEvery { mealFirebaseRepository.deleteMeal("1") } returns Tasks.forResult(Unit)
        coEvery { mealDao.deleteById("1") } returns Unit
        coEvery { checkConnectivity.checkConnection() } returns true

        mealRepository.deleteMeal("1")
      }

  @Test
  fun outdateddata() = runTest {
    coEvery { checkConnectivity.checkConnection() } returns false
    coEvery { mealDao.insert(any<Meal>()) } returns "109"
    val docId = mealRepository.storeMeal(Meal.default())

    assertEquals(null, docId)
  }

  @Test
  fun getALlIngredients() = runTest {
    val ingredientList = listOf(Meal.default().ingredients).flatten()
    every { mealDao.getAllIngredients("testId") } returns ingredientList
    val result = mealRepository.getAllIngredients()
    assertEquals(ingredientList, result)
  }

  @Test
  fun outdatedDataIsWritenToFirebase() = runTest {
    var counter = 0
    every { checkConnectivity.checkConnection() } answers
        {
          var returnVale = true
          if (counter == 1) {
            returnVale = false
          }
          counter++

          returnVale
        }

    val mockDoc = mockk<DocumentReference>()
    coEvery { mealFirebaseRepository.storeMeal(any()) } returns Tasks.forResult(mockDoc)
    coEvery { mealDao.getAllMeals(any()) } returns listOf(Meal.default())
    coEvery { mealDao.insert(any<Meal>()) } returns "109"

    mealRepository.storeMeal(Meal.default())
    val result = mealRepository.storeMeal(Meal.default())
    assertNull(result)
    mealRepository.storeMeal(Meal.default())

    verify { mealFirebaseRepository.storeMeal(any()) }
  }

  @Test
  fun deletedMealOutdated() = runTest {
    var counter = 0
    every { checkConnectivity.checkConnection() } answers
        {
          var returnVale = true
          if (counter == 1) {
            returnVale = false
          }
          counter++

          returnVale
        }

    val mockDoc = mockk<DocumentReference>()
    coEvery { mealFirebaseRepository.deleteMeal(any()) } returns Tasks.forResult(Unit)
    coEvery { mealFirebaseRepository.storeMeal(any()) } returns Tasks.forResult(mockDoc)
    coEvery { mealDao.getAllMeals(any()) } returns listOf(Meal.default())
    coEvery { mealDao.insert(any<Meal>()) } returns "109"
    coEvery { mealDao.deleteById(any()) } returns Unit

    mealRepository.storeMeal(Meal.default())
    val result = mealRepository.storeMeal(Meal.default())
    assertNull(result)
    mealRepository.deleteMeal("1")

    verify { mealFirebaseRepository.deleteMeal("1") }
  }

  @Test
  fun getMealById() = runTest {
    val default = Meal.default()
    every { mealDao.getMealById(any()) } returns default
    val result = mealRepository.getMealById("1")
    assertEquals(default, result)
  }
}
