package com.github.se.polyfit.data.user

import com.github.se.polyfit.model.data.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserTest {

  @Test
  fun `User creation with null email`() {
    val user = User("1", "Test User", "Test", "User", "null", null)
    assertEquals("1", user.id)
    assertEquals("Test User", user.displayName)
    assertEquals("Test", user.familyName)
    assertEquals("User", user.givenName)
    assertEquals("null", user.email)
  }

  @Test
  fun `User creation with invalid email`() {
    val user = User("1", "Test User", "Test", "User", "invalid email", null)
    assertEquals("1", user.id)
    assertEquals("Test User", user.displayName)
    assertEquals("Test", user.familyName)
    assertEquals("User", user.givenName)
    assertEquals("invalid email", user.email)
    assertNull(user.photoURL)
  }
}
