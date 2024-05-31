package com.github.se.polyfit.model.post

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.google.firebase.storage.StorageReference
import java.time.LocalDate

/** Represents a post in the application. Made to be immutable to avoid any changes to the post. */
interface UnmodifiablePost {
  val userId: String
  val description: String
  val location: Location
  val meal: Meal
  val createdAt: LocalDate
}

/**
 * Represents a post in the application. Made to be mutable to allow changes to the post.
 *
 * @param userId The id of the user who created the post.
 * @param description The description of the post.
 * @param location The location of the post.
 * @param meal The meal of the post.
 * @param createdAt The date the post was created.
 * @param listOfImages The list of images in the post.
 * @param listOfURLs The list of URLs of the images in the post.
 * @param imageDownloadURL The URL of the image in the post (on the firebase Storage).
 */
data class Post(
    override var userId: String,
    override var description: String,
    override var location: Location,
    override var meal: Meal,
    override var createdAt: LocalDate,
    var listOfImages: List<Bitmap> = emptyList(),
    var listOfURLs: List<StorageReference> = emptyList(),
    var imageDownloadURL: Uri = Uri.EMPTY
) : UnmodifiablePost {

  fun getCarbs(): Nutrient? {
    return meal.getNutrient("carbohydrates")
  }

  fun getFat(): Nutrient? {
    return meal.getNutrient("fat")
  }

  fun getProtein(): Nutrient? {
    return meal.getNutrient("protein")
  }

  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

  // Useful function for the ui
  fun getIngredientCalories(): List<Pair<String, Nutrient>> {
    return meal.ingredients.mapNotNull { ingredient ->
      val nutrient =
          ingredient.nutritionalInformation.nutrients.firstOrNull { it.nutrientType == "calories" }
      if (nutrient != null) Pair(ingredient.name, nutrient) else null
    }
  }

  // Really useful function for the ui
  fun getIngredientWeight(): List<Pair<String, Nutrient>> {
    return meal.ingredients.mapNotNull { ingredient ->
      Pair(ingredient.name, Nutrient("totalWeight", ingredient.amount, ingredient.unit))
    }
  }

  companion object {
    fun serialize(data: Post): Map<String, Any> {
      return mutableMapOf<String, Any>().apply {
        this["userId"] = data.userId
        this["description"] = data.description
        this["location"] = data.location.serialize()
        this["meal"] = data.meal.serialize()
        this["createdAt"] = data.createdAt.toString()
        this["imageDownloadURL"] = data.imageDownloadURL
      }
    }

    /**
     * Deserializes a map to a Post object.
     *
     * @param data The map to deserialize.
     * @return The Post object.
     * @throws IllegalArgumentException if cannot deserialize the map.
     */
    fun deserialize(data: Map<String, Any?>): Post? {
      return try {
        val userId = data["userId"] as String

        val description = data["description"] as? String ?: ""
        val location = Location.deserialize(data["location"] as Map<String, Any>)
        val meal = Meal.deserialize(data["meal"] as Map<String, Any>)
        val createdAt = LocalDate.parse(data["createdAt"] as String)
        var imageDownloadURL: Uri = Uri.parse(data["imageDownloadURL"].toString())

        val newPost =
            Post(
                userId, description, location, meal, createdAt, imageDownloadURL = imageDownloadURL)

        newPost
      } catch (e: Exception) {
        Log.e("Post", "Failed to deserialize Post object: ${e.message}", e)
        throw IllegalArgumentException("Failed to deserialize Post object", e)
      }
    }

    fun default(): Post {

      return Post(
          userId = "testId",
          description = "Description",
          location = Location(0.0, 0.0, 10.0, "EPFL"),
          meal = Meal.default(),
          createdAt = LocalDate.now())
    }
  }
}
