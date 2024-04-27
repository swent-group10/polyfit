package com.github.se.polyfit.model.post

import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class PostTest {

  @Test
  fun `getCarbs returns correct nutrient when present`() {
    val expected = Nutrient("carb", 100.0, MeasurementUnit.G)
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
  fun `toString outputs expected format`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())
    val expectedString =
        "The post from the user userId with the following description description and the following location Location(longitude=0.0, latitude=0.0, altitude=10.0, name=EPFL) for following meal $meal"
    assertEquals(expectedString, post.toString())
  }

  @Test
  fun `default method returns expected Post object`() {
    val post = Post.default()
    val expectedPost =
        Post(
            "testId",
            "Description",
            Location(0.0, 0.0, 10.0, "EPFL"),
            Meal.default(),
            LocalDate.now())

    assertEquals(expectedPost, post)
  }

  @Test
  fun `getIngredientWeight returns correct list`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())
    val expectedList =
        meal.ingredients.map { it.name to Nutrient("totalWeight", it.amount, it.unit) }

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
  fun testLocationToString() {
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
  fun `deserialize location`() {
    val location =
        Location.deserialize(
            mapOf("longitude" to 0.0, "latitude" to 0.0, "altitude" to 10.0, "name" to "EPFL"))
    val expectedLocation = Location(0.0, 0.0, 10.0, "EPFL")
    assertEquals(expectedLocation, location)
  }
}
