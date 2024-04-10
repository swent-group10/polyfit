package com.github.se.polyfit.model.data

import android.net.Uri

data class User(
    val id: String,
    val displayName: String,
    val familyName: String,
    val getGivenName: String,
    val email: String,
    val photoURL: Uri?
) {
  companion object {
    fun resetCurrentUser() {
      currentUser = null
    }

    var currentUser: User? = null
  }
}
