package com.github.se.polyfit.model.post

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import java.time.LocalDate
import kotlin.test.assertFails
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PostTest {

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any<String>(), any<String>(), any()) } returns 0
  }

  @Test
  fun `getCarbs returns correct nutrient when present`() {
    val expected = Nutrient("carbohydrates", 100.0, MeasurementUnit.G)
    val meal = Meal.default().apply { nutritionalInformation.update(expected) }
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(expected, post.getCarbs())
  }

  @Test
  fun `getCarbs returns null when nutrient not present`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(null, post.getCarbs())
  }

  @Test
  fun `serialize returns correct map`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())
    val expectedMap =
        mapOf(
            "userId" to "userId",
            "description" to "description",
            "location" to Location(0.0, 0.0, 10.0, "EPFL"),
            "meal" to meal.serialize(),
            "createdAt" to LocalDate.now())

    assertEquals(expectedMap, post.serialize())
  }

  @Test
  fun `getIngredientCalories returns correct list when nutrients present`() {
    val nutrient = Nutrient("calories", 100.0, MeasurementUnit.KCAL)
    val meal = Meal.default()
    val ingredient =
        Ingredient(
            "meal",
            100,
            100.0,
            MeasurementUnit.KCAL,
            NutritionalInformation(mutableListOf(nutrient)))
    meal.ingredients.add(ingredient)
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())
    val expectedList = listOf(Pair("meal", nutrient))

    assertEquals(expectedList, post.getIngredientCalories())
  }

  @Test
  fun `getIngredientCalories returns empty list when nutrients not present`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(emptyList<Pair<String, Nutrient>>(), post.getIngredientCalories())
  }

  @Test
  fun `default method returns expected Post object`() {
    val defaultMeal = Meal.default()
    val post = Post.default().copy(meal = defaultMeal)
    val expectedPost =
        Post(
            "testId", "Description", Location(0.0, 0.0, 10.0, "EPFL"), defaultMeal, LocalDate.now())

    assertEquals(expectedPost, post)
  }

  @Test
  fun `getIngredientWeight returns correct list`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())
    val expectedList =
        meal.ingredients.mapNotNull { it.name to Nutrient("totalWeight", it.amount, it.unit) }

    assertEquals(expectedList, post.getIngredientWeight())
  }

  @Test
  fun `getProtein returns correct nutrient when present`() {
    val expected = Nutrient("protein", 100.0, MeasurementUnit.G)
    val meal = Meal.default().apply { nutritionalInformation.update(expected) }
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(expected, post.getProtein())
  }

  @Test
  fun `getFat returns correct nutrient when present`() {
    val expected = Nutrient("fat", 100.0, MeasurementUnit.G)
    val meal = Meal.default().apply { nutritionalInformation.update(expected) }
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(expected, post.getFat())
  }

  @Test
  fun `testLocationToString`() {
    val location = Location(0.0, 0.0, 10.0, "EPFL")
    val expectedString = "Location(longitude=0.0, latitude=0.0, altitude=10.0, name=EPFL)"
    assertEquals(expectedString, location.toString())
  }

  @Test
  fun `test default location`() {
    val location = Location.default()
    val expectedLocation = Location(0.0, 0.0, 0.0, "EPFL")
    assertEquals(expectedLocation, location)
  }

  @Test
  fun deserializePost() {
    val defaultMeal = Meal.default()
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "userId",
                "description" to "description",
                "location" to
                    mapOf(
                        "longitude" to 0.0,
                        "latitude" to 0.0,
                        "altitude" to 10.0,
                        "name" to "EPFL"),
                "meal" to defaultMeal.serialize(),
                "createdAt" to
                    mapOf(
                        "year" to 2021.toLong(),
                        "monthValue" to 10.toLong(),
                        "dayOfMonth" to 10.toLong())))
    val expectedPost =
        Post(
            "userId",
            "description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            defaultMeal,
            LocalDate.of(2021, 10, 10))
    assertEquals(expectedPost, post)
  }

  @Test
  fun deserializePostWithDifferentLocation() {
    val defaultMeal = Meal.default()
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "userId",
                "description" to "description",
                "location" to
                    mapOf(
                        "longitude" to 10.0,
                        "latitude" to 20.0,
                        "altitude" to 30.0,
                        "name" to "MIT"),
                "meal" to defaultMeal.serialize(),
                "createdAt" to
                    mapOf(
                        "year" to 2021.toLong(),
                        "monthValue" to 10.toLong(),
                        "dayOfMonth" to 10.toLong())))
    val expectedPost =
        Post(
            "userId",
            "description",
            Location(10.0, 20.0, 30.0, "MIT"),
            defaultMeal,
            LocalDate.of(2021, 10, 10))
    assertEquals(expectedPost, post)
  }

  @Test
  fun deserializePostWithDifferentUserId() {
    val defaultMeal = Meal.default()
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "differentUserId",
                "description" to "description",
                "location" to
                    mapOf(
                        "longitude" to 0.0,
                        "latitude" to 0.0,
                        "altitude" to 10.0,
                        "name" to "EPFL"),
                "meal" to defaultMeal.serialize(),
                "createdAt" to
                    mapOf(
                        "year" to 2021.toLong(),
                        "monthValue" to 10.toLong(),
                        "dayOfMonth" to 10.toLong())))
    val expectedPost =
        Post(
            "differentUserId",
            "description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            defaultMeal,
            LocalDate.of(2021, 10, 10))
    assertEquals(expectedPost, post)
  }

  @Test
  fun deserializePostWithDifferentDescription() {
    val defaultMeal = Meal.default()
    val post =
        Post.deserialize(
            mapOf(
                "userId" to "userId",
                "description" to "differentDescription",
                "location" to
                    mapOf(
                        "longitude" to 0.0,
                        "latitude" to 0.0,
                        "altitude" to 10.0,
                        "name" to "EPFL"),
                "meal" to defaultMeal.serialize(),
                "createdAt" to
                    mapOf(
                        "year" to 2021.toLong(),
                        "monthValue" to 10.toLong(),
                        "dayOfMonth" to 10.toLong())))
    val expectedPost =
        Post(
            "userId",
            "differentDescription",
            Location(0.0, 0.0, 10.0, "EPFL"),
            defaultMeal,
            LocalDate.of(2021, 10, 10))
    assertEquals(expectedPost, post)
  }

  @Test
  fun deserializeThrowsException() {
    val data =
        mapOf(
            "userId" to null,
            "description" to "description",
            "location" to
                mapOf("longitude" to 0.0, "latitude" to 0.0, "altitude" to 10.0, "name" to "EPFL"),
            "meal" to Meal.default().serialize(),
            "createdAt" to LocalDate.now())

    assertFails { Post.deserialize(data) }
  }
}
