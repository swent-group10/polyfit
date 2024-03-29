package com.github.se.polyfit.model.ingredient

import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

/**
 * Represents an ingredient in a meal. Automatically updates the parent meal when any property
 * changes.
 *
 * @property name is unique for each meal
 */
data class Ingredient(
    val name: String, // name is now a val
    val nutritionalInformation: NutritionalInformation,
) {

    companion object {
        fun serializeIngredient(ingredient: Ingredient): Map<String, Any> {
            val map = mutableMapOf<String, Any>()
            map["name"] = ingredient.name
            map["nutritionalInformation"] =
                NutritionalInformation.serialize(ingredient.nutritionalInformation)
            return map
        }

        fun deserializeIngredient(data: Map<String, Any>): Ingredient {

            try {
                val name = data["name"] as String

                val nutritionalInformation =
                    NutritionalInformation.deserialize(
                        data["nutritionalInformation"] as
                                Map<String, Map<String, Any>>
                    )
                return Ingredient(name, nutritionalInformation)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid ingredient")
            }
        }
    }
}
