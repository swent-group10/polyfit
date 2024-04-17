import com.github.se.polyfit.data.repository.MealRepository

class MealRepositoryTest {

    private lateinit var mealRepository: MealRepository

//  @Before
//  fun setup() {
//      mealRepository = mockk()
//    mealRepository = MealRepository(mealRepository)
//  }
//
//  @Test
//  fun storeMeal_successfullyStoresMeal() = runTest {
//    val meal =
//        Meal(
//            MealOccasion.DINNER,
//            "name",
//            1,
//            12.0,
//            NutritionalInformation(mutableListOf()),
//            mutableListOf())
//    val documentReference = mockk<DocumentReference>()
//    coEvery { mealRepository.storeMeal(meal) } returns Tasks.forResult(documentReference)
//
//    mealRepository.storeMeal(meal)
//  }
//
//  @Test
//  fun getMeal_returnsExpectedMeal() = runTest {
//    val expectedMeal =
//        Meal(
//            MealOccasion.DINNER,
//            "name",
//            1,
//            12.0,
//            NutritionalInformation(mutableListOf()),
//            mutableListOf())
//
//    coEvery { mealFirebaseRepository.getMeal("id") } returns Tasks.forResult(expectedMeal)
//
//    val actualMeal = mealRepository.getMeal("id")
//
//    assertEquals(expectedMeal, actualMeal)
//  }
//
//  @Test
//  fun getAllMeals_returnsExpectedMeals() = runTest {
//    val expectedMeals =
//        listOf(
//            Meal(
//                MealOccasion.DINNER,
//                "name1",
//                1,
//                1.2,
//                NutritionalInformation(mutableListOf()),
//            ),
//            Meal(MealOccasion.DINNER, "name2", 2, 1.3, NutritionalInformation(mutableListOf())))
//    coEvery { mealFirebaseRepository.getAllMeals() } returns Tasks.forResult(expectedMeals)
//
//    val actualMeals = mealRepository.getAllMeals()
//
//    assertEquals(expectedMeals, actualMeals)
//  }
//
//  @Test
//  fun deleteMeal_successfullyDeletesMeal() = runTest {
//    coEvery { mealFirebaseRepository.deleteMeal("id") } returns Tasks.forResult(null)
//
//    mealRepository.deleteMeal("id")
//  }
}
