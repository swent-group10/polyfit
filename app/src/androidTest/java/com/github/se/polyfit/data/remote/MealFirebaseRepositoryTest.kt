import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Tasks
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class MealFirebaseRepositoryTest {

    private val db = MealFirebaseRepository("test")
    private val meal = Meal(
        occasion = MealOccasion.BREAKFAST,
        name = "Test Meal",
        mealID = 0,
        nutritionalInformation = NutritionalInformation(
            mutableListOf(
                Nutrient("Test", 0.0, MeasurementUnit.UNIT),
                Nutrient("Test2", 0.0, MeasurementUnit.UNIT)

            )
        )
    )

    @Before
    fun setUp() {
        meal.addIngredient(
            Ingredient(
                name = "Test Ingredient",
                id = 0,
                nutritionalInformation = NutritionalInformation(
                    mutableListOf(
                        Nutrient("Test", 0.0, MeasurementUnit.UNIT),
                        Nutrient("Test2", 0.0, MeasurementUnit.UNIT)
                    )
                )
            )
        )
    }

    @Test
    fun storeMeal() {
        val task = db.storeMeal(meal)
        val result = Tasks.await(task)
        assertNotNull(result)
    }

    @Test
    fun getAllMeals() {
        //make sure we are given a list of two meals
        val task = db.getAllMeals()
        val result = Tasks.await(task)
        assertNotNull(result)


    }

    @Test
    fun getOneMeal() {
        //make sure we are given a list of two meals
        val task = db.getMeal("0")
        val result = Tasks.await(task)
        assertNotNull(result)
    }
}
