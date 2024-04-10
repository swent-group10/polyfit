package com.github.se.polyfit.viewmodel.meal

import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient

class MealViewModel(userID: String) {
    // after friday use hilt dependency injection to make code cleaner, for now
    // i gues this is ok
    private val mealRepo: MealRepository = MealRepository(userID)

    fun getAllMeals() {
    }

    fun getIngredientsFromImage(image: String) {}

    fun setIngredients(mealID: Long, ingredient: Ingredient) {}

    fun getAllIngredients() {

    }


}


/*
features requested by mesha : Sweet!


getIngredients (for initial display, i guess either a list of all ingredients or if we support it, one for confirmed and one for potential ingredients)
addIngredient (add from potential ingredient list, or from the add button)
removeIngredient (for when expanding the ingredient is supported)



 */