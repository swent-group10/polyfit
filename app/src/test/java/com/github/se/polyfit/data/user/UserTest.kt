package com.github.se.polyfit.data.user

import com.github.se.polyfit.model.data.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserTest {

  @Test
  fun `User creation with valid data`() {
    val user = User("1", "Test User", "Test", "User", "test@example.com", null)
    assertEquals("1", user.id)
    assertEquals("Test User", user.displayName)
    assertEquals("Test", user.familyName)
    assertEquals("User", user.getGivenName)
    assertEquals("test@example.com", user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `currentUser is null by default`() {
    assertNull(User.currentUser)
  }

  @Test
  fun `currentUser can be set`() {
    val user = User("10000", "Test User", "Test", "User", "test@example.com", null)
    User.currentUser = user
    assertEquals(user, User.currentUser)
  }

  @Test
  fun `resetCurrentUser sets currentUser to null`() {
    val user = User("10000", "Test User", "Test", "User", "test@example.com", null)
    User.currentUser = user
    User.resetCurrentUser()
    assertNull(User.currentUser)
  }
}
