package com.github.se.polyfit.model.data

import android.net.Uri
import android.util.Log

data class User(
    var id: String = "",
    var displayName: String? = null,
    var familyName: String? = null,
    var givenName: String? = null,
    var email: String = "",
    var photoURL: Uri? = null
) {

  fun signOut() {
    id = ""
    displayName = null
    familyName = null
    givenName = null
    email = ""
    photoURL = null
  }

  fun isSignedIn(): Boolean {
    return id.isNotEmpty()
  }

  fun serialize(): Map<String, Any> {
    return serialize(this)
  }

  fun update(user: User) {
    update(
        id = user.id,
        displayName = user.displayName,
        familyName = user.familyName,
        givenName = user.givenName,
        email = user.email,
        photoURL = user.photoURL)
  }

  fun update(
      id: String = this.id,
      displayName: String? = this.displayName,
      familyName: String? = this.familyName,
      givenName: String? = this.givenName,
      email: String = this.email,
      photoURL: Uri? = this.photoURL
  ) {
    this.id = id
    this.displayName = displayName
    this.familyName = familyName
    this.givenName = givenName
    this.email = email
    this.photoURL = photoURL
  }

  companion object {
    fun serialize(user: User): Map<String, Any> {
      return mapOf(
          "id" to user.id,
          "displayName" to user.displayName!!,
          "familyName" to user.familyName!!,
          "givenName" to user.givenName!!,
          "email" to user.email,
          "photoURL" to user.photoURL.toString())
    }

    fun deserialize(map: Map<String, Any>): User {
      return try {
        User(
            id = map["id"] as String,
            displayName = map["displayName"] as String,
            familyName = map["familyName"] as String,
            givenName = map["givenName"] as String,
            email = map["email"] as String,
            photoURL =
                if (map["photoURL"] != "null") Uri.parse(map["photoURL"] as String) else Uri.EMPTY)
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
          email = "somethingsomething@google.ze")
    }
  }
}
