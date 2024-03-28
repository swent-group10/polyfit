package com.github.se.polyfit.model.meal

class MealTest {
//
//  private lateinit var meal: Meal
//  private lateinit var expectedMap: Pair<Map<String, Any>, List<Map<String, Any>>>
//
//  private val ingredient1 = Ingredient("eggs", 1.0, 102.0, 1231.0, 1234.0, 12303.0)
//  private val ingredient2 = Ingredient("milk", 1.0, 102.0, 1231.0, 1234.0, 12303.0)
//  private val ingredient3 = Ingredient("bread", 1.0, 102.0, 1231.0, 1234.0, 12303.0)
//
//  private val mealWithManyIngredients =
//      Meal(
//          MealOccasion.DINNER,
//          "eggs",
//          102.0,
//          1231.0,
//          1234.0,
//          12303.0,
//      )
//
//  @Before
//  fun setUp() {
//    val uid = "1"
//    val name = "eggs"
//    val calories = 102.2
//    val protein = 12301.3
//    val carbohydrates = 1234.9
//    val fat = 12303.0
//
//    val ingredientEgg = Ingredient(name, 1.2, calories, protein, carbohydrates, fat)
//    meal = Meal(MealOccasion.DINNER, name, calories, protein, carbohydrates, fat)
//    meal.updateUid(uid)
//    meal.addIngredient(ingredientEgg)
//
//    expectedMap =
//        Pair(
//            mapOf(
//                "uid" to uid,
//                "occasion" to MealOccasion.DINNER.name,
//                "name" to name,
//                "calories" to calories,
//                "protein" to protein,
//                "carbohydrates" to carbohydrates,
//                "fat" to fat,
//            ),
//            listOf(Ingredient.serializeIngredient(ingredientEgg)))
//
//    mealWithManyIngredients.addIngredient(ingredient1)
//    mealWithManyIngredients.addIngredient(ingredient2)
//    mealWithManyIngredients.addIngredient(ingredient3)
//  }
//
//  @Test
//  fun `Meal serialization produces expected map`() {
//    val map = Meal.serializeMeal(meal)
//    assertThat(map, equalTo(expectedMap))
//  }
//
//  @Test
//  fun `Meal deserialization produces expected meal`() {
//    val mealDeserialized = Meal.deserializeMeal(expectedMap.first, expectedMap.second)
//    assertThat(meal, equalTo(mealDeserialized))
//  }
//
//  @Test
//  fun `Adding ingredient updates meal nutrition values`() {
//    val ingredient = Ingredient("eggs", 1.2, 102.2, 12301.3, 1234.9, 12303.0)
//    val meal =
//        Meal(
//            MealOccasion.DINNER, "eggs", 102.2, 12301.3, 1234.9, 12303.0, mutableListOf(ingredient))
//    val ingredient2 = Ingredient("eggs", 1.2, 102.2, 12301.3, 1234.9, 12303.0)
//    meal.addIngredient(ingredient2)
//    assertThat(meal.calories, equalTo(204.4))
//    assertThat(meal.protein, equalTo(24602.6))
//    assertThat(meal.carbohydrates, equalTo(2469.8))
//    assertThat(meal.fat, equalTo(24606.0))
//  }
//
//  @Test
//  fun `Serialization of meal with multiple ingredients produces expected map`() {
//    assertThat(mealWithManyIngredients.calories.toInt(), equalTo(306))
//    assertThat(mealWithManyIngredients.protein.toInt(), equalTo(3693))
//    assertThat(mealWithManyIngredients.carbohydrates.toInt(), equalTo(3702))
//    assertThat(mealWithManyIngredients.fat.toInt(), equalTo(36909))
//
//    val serializedMeal = Meal.serializeMeal(mealWithManyIngredients)
//    val expectedMealPair =
//        Pair(
//            mapOf(
//                "uid" to "",
//                "occasion" to MealOccasion.DINNER.name,
//                "name" to "eggs",
//                "calories" to 306.0,
//                "protein" to 3693.0,
//                "carbohydrates" to 3702.0,
//                "fat" to 36909.0,
//            ),
//            listOf(
//                Ingredient.serializeIngredient(ingredient1),
//                Ingredient.serializeIngredient(ingredient2),
//                Ingredient.serializeIngredient(ingredient3)))
//    assertThat(serializedMeal, equalTo(expectedMealPair))
//  }
//
//  @Test
//  fun `Deserialization of meal with multiple ingredients produces expected meal`() {
//    val serializedMeal = Meal.serializeMeal(mealWithManyIngredients)
//    val deserializedMeal = Meal.deserializeMeal(serializedMeal.first, serializedMeal.second)
//
//    assertThat(deserializedMeal, equalTo(mealWithManyIngredients))
//  }
}
