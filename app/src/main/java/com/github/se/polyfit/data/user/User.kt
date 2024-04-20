package com.github.se.polyfit.model.data

import android.net.Uri
import java.util.concurrent.atomic.AtomicReference

data class User(
    var id: String = "",
    var displayName: String? = null,
    var familyName: String? = null,
    var givenName: String? = null,
    var email: String = "",
    var photoURL: Uri? = null
) {
  companion object {

    private val currentUser = AtomicReference<User?>()

    fun resetCurrentUser() {
      currentUser.set(null)
    }

    fun getCurrentUser(): User? = currentUser.get()

    fun setCurrentUser(user: User) {
      val wasSet = currentUser.compareAndSet(null, user)
      if (!wasSet) {
        throw IllegalStateException("Cannot set new user as currentUser is already set.")
      }
    }
  }
}
