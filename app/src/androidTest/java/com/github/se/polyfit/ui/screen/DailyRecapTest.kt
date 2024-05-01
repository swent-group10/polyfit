package com.github.se.polyfit.ui.components.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.meal.MealTagColor
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.screen.DailyRecapScreen
import com.github.se.polyfit.ui.screen.DailyRecapTopBar
import com.github.se.polyfit.viewmodel.dailyRecap.DailyRecapViewModel
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import java.time.LocalDate
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DailyRecapTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  private lateinit var mealsByDate: MutableMap<LocalDate, List<Meal>>
  private var date: LocalDate = LocalDate.now()

  private val viewModel =
      mockk<DailyRecapViewModel>(relaxed = true) {
        mealsByDate =
            mutableMapOf(
                date to
                    listOf(
                        Meal(
                            mealID = 1,
                            name = "Meal 1",
                            ingredients =
                                mutableListOf(
                                    Ingredient(
                                        name = "Tomato",
                                        id = 1,
                                        amount = 100.0,
                                        unit = MeasurementUnit.UNIT,
                                        nutritionalInformation =
                                            NutritionalInformation(
                                                mutableListOf(
                                                    Nutrient(
                                                        "calories", 10.0, MeasurementUnit.KCAL))))),
                            tags =
                                mutableListOf(
                                    MealTag("This is a long tag name", MealTagColor.BABYPINK),
                                    MealTag("And yet another long", MealTagColor.LAVENDER),
                                    MealTag(
                                        "And yet another long long", MealTagColor.BRIGHTORANGE)),
                            occasion = MealOccasion.BREAKFAST,
                            nutritionalInformation = NutritionalInformation(mutableListOf())),
                    ),
                LocalDate.now().minusDays(1) to
                    listOf(
                        Meal(
                            mealID = 2,
                            name = "Meal 2",
                            ingredients =
                                mutableListOf(
                                    Ingredient(
                                        name = "Tomato",
                                        id = 1,
                                        amount = 100.0,
                                        unit = MeasurementUnit.UNIT,
                                        nutritionalInformation =
                                            NutritionalInformation(
                                                mutableListOf(
                                                    Nutrient(
                                                        "calories", 10.0, MeasurementUnit.KCAL))))),
                            tags =
                                mutableListOf(
                                    MealTag("This is a long tag name", MealTagColor.BABYPINK),
                                    MealTag("And yet another long", MealTagColor.LAVENDER),
                                    MealTag(
                                        "And yet another long long", MealTagColor.BRIGHTORANGE)),
                            occasion = MealOccasion.BREAKFAST,
                            nutritionalInformation = NutritionalInformation(mutableListOf())),
                    ))

        every { getMealsOnDate(any()) } answers
            {
              val date = arg<LocalDate>(0)
              val newMeal = mealsByDate[date] ?: emptyList()
              every { meals.value } returns newMeal
              Unit
            }
        every { meals.value } returns (mealsByDate[date] ?: emptyList())
      }

  fun setContent(
      navigateBack: () -> Unit = mockNav::goBack,
      navigateTo: () -> Unit = mockNav::navigateToAddMeal,
      viewModel: DailyRecapViewModel = this.viewModel,
      isFetching: Boolean = false
  ) {
    every { viewModel.isFetching.value } returns isFetching
    composeTestRule.setContent { DailyRecapScreen(navigateBack, navigateTo, viewModel, mockk()) }
  }

  @Test
  fun everythingDisplayWhileFetching() {
    setContent(isFetching = true)
    ComposeScreen.onComposeScreen<DailyRecapScreen>(composeTestRule) {
      spinner { assertIsDisplayed() }
    }

    ComposeScreen.onComposeScreen<DailyRecapTopBar>(composeTestRule) {
      title {
        assertIsDisplayed()
        assertTextEquals("Overview")
      }

      backButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun mealIsDisplayed() {
    setContent()

    ComposeScreen.onComposeScreen<DailyRecapScreen>(composeTestRule) {
      spinner { assertDoesNotExist() }
    }

    ComposeScreen.onComposeScreen<MealListScreen>(composeTestRule) {
      mealCard { assertIsDisplayed() }
      mealName {
        assertIsDisplayed()
        assertTextEquals("Meal 1")
      }
      mealCalories {
        assertIsDisplayed()
        assertTextEquals("10.0 kcal")
      }
    }
  }

  @Test
  fun noRecordedMeals() {
    val mockViewModel = mockk<DailyRecapViewModel>(relaxed = true)
    every { mockViewModel.meals.value } returns emptyList()
    setContent(viewModel = mockViewModel)

    ComposeScreen.onComposeScreen<DailyRecapScreen>(composeTestRule) {
      spinner { assertDoesNotExist() }

      noRecordedMeals {
        assertIsDisplayed()
        assertTextEquals("No recorded meals")
      }
    }
  }
}
