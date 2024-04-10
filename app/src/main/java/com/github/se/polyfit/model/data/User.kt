package com.github.se.polyfit.model.data

import android.net.Uri

data class User(val id: String, val name: String, val email: String, val photoURL: Uri?) {
  companion object {
    var currentUser: User? = null
  }

  fun resetCurrentUser() {
    currentUser = null
  }
}
