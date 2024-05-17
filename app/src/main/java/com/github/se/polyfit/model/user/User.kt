package com.github.se.polyfit.model.data

import android.net.Uri
import android.util.Log
import java.time.LocalDate

data class User(
    var id: String = "",
    var displayName: String? = null,
    var familyName: String? = null,
    var givenName: String? = null,
    var email: String = "",
    var photoURL: Uri? = null,
    var heightCm: Long? = null,
    var weightKg: Double? = null,
    var dob: LocalDate? = null,
    var calorieGoal: Long = 2200, // Not sure what we should have this default to
    var isVegan: Boolean = false,
    var isVegetarian: Boolean = false,
    var isGlutenFree: Boolean = false,
    var isLactoseFree: Boolean = false,
) {

  fun signOut() {
    update(User()) // Creates an empty user and then updates the current user with it
  }

  fun isSignedIn(): Boolean {
    return id.isNotEmpty() && !id.equals("testUserID")
  }

  fun serialize(): Map<String, Any?> {
    return serialize(this)
  }

  fun update(user: User) {
    update(
        id = user.id,
        displayName = user.displayName,
        familyName = user.familyName,
        givenName = user.givenName,
        email = user.email,
        photoURL = user.photoURL,
        heightCm = user.heightCm,
        weightKg = user.weightKg,
        dob = user.dob,
        calorieGoal = user.calorieGoal,
        isVegan = user.isVegan,
        isVegetarian = user.isVegetarian,
        isGlutenFree = user.isGlutenFree,
        isLactoseFree = user.isLactoseFree)
  }

  fun update(
      id: String = this.id,
      displayName: String? = this.displayName,
      familyName: String? = this.familyName,
      givenName: String? = this.givenName,
      email: String = this.email,
      photoURL: Uri? = this.photoURL,
      heightCm: Long? = this.heightCm,
      weightKg: Double? = this.weightKg,
      dob: LocalDate? = this.dob,
      calorieGoal: Long = this.calorieGoal,
      isVegan: Boolean = this.isVegan,
      isVegetarian: Boolean = this.isVegetarian,
      isGlutenFree: Boolean = this.isGlutenFree,
      isLactoseFree: Boolean = this.isLactoseFree
  ) {
    this.id = id
    this.displayName = displayName
    this.familyName = familyName
    this.givenName = givenName
    this.email = email
    this.photoURL = photoURL
    this.heightCm = heightCm
    this.weightKg = weightKg
    this.dob = dob
    this.calorieGoal = calorieGoal
    this.isVegan = isVegan
    this.isVegetarian = isVegetarian
    this.isGlutenFree = isGlutenFree
    this.isLactoseFree = isLactoseFree
  }

  companion object {
    fun serialize(user: User): Map<String, Any?> {
      // To serialize/store, they must have atleast id, email, and names.
      val result =
          mutableMapOf<String, Any?>(
              "id" to user.id,
              "displayName" to user.displayName!!,
              "familyName" to user.familyName!!,
              "givenName" to user.givenName!!,
              "email" to user.email,
              "calorieGoal" to user.calorieGoal,
              "isVegan" to user.isVegan,
              "isVegetarian" to user.isVegetarian,
              "isGlutenFree" to user.isGlutenFree,
              "isLactoseFree" to user.isLactoseFree)

      user.photoURL?.let { result["photoURL"] = it.toString() }
      user.heightCm?.let { result["heightCm"] = it }
      user.weightKg?.let { result["weightKg"] = it }
      user.dob?.let { result["dob"] = it.toString() }

      return result
    }

    fun deserialize(map: Map<String, Any?>): User {
      return try {
        User(
            id = map["id"] as String,
            displayName = map["displayName"] as? String,
            familyName = map["familyName"] as? String,
            givenName = map["givenName"] as? String,
            email = map["email"] as String,
            photoURL = (map["photoURL"] as? String)?.let { Uri.parse(it) },
            heightCm = map["heightCm"] as? Long,
            weightKg = map["weightKg"] as? Double,
            dob = (map["dob"] as? String)?.let { LocalDate.parse(it) },
            calorieGoal = map["calorieGoal"] as? Long ?: 2200,
            isVegan = map["isVegan"] as? Boolean ?: false,
            isVegetarian = map["isVegetarian"] as? Boolean ?: false,
            isGlutenFree = map["isGlutenFree"] as? Boolean ?: false,
            isLactoseFree = map["isLactoseFree"] as? Boolean ?: false)
      } catch (e: Exception) {
        Log.e("User", "Error deserializing user", e)
        throw IllegalArgumentException("Error deserializing user : $e")
      }
    }

    fun testUser(): User {
      return User(
          id = "testId",
          displayName = "Test User",
          familyName = "User",
          givenName = "Test",
          email = "somethingsomething@google.ze",
          heightCm = 180,
          weightKg = 80.0,
          dob = LocalDate.of(1990, 1, 1),
          calorieGoal = 2200,
          isVegan = false,
          isVegetarian = false,
          isGlutenFree = false,
          isLactoseFree = false)
    }
  }
}
