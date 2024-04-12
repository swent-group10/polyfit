package com.github.se.polyfit.model.data

import android.net.Uri
import java.util.concurrent.atomic.AtomicReference

data class User(
    val id: String,
    val displayName: String?,
    val familyName: String?,
    val givenName: String?,
    val email: String,
    val photoURL: Uri?
) {
  companion object {

    private val currentUser = AtomicReference<User?>()

    fun resetCurrentUser() {
      currentUser.set(null)
    }

    fun getCurrentUser(): User? = currentUser.get()

    fun setCurrentUser(user: User) {
      currentUser.set(user)
    }
  }
}
