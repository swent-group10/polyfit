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
    assertEquals("User", user.givenName)
    assertEquals("test@example.com", user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `currentUser is null by default`() {
    User.resetCurrentUser()
    assertNull(User.getCurrentUser())
  }

  @Test
  fun `currentUser can be set`() {
    val user = User("10000", "Test User", "Test", "User", "test@example.com", null)
    User.setCurrentUser(user)
    assertEquals(user, User.getCurrentUser())
    User.resetCurrentUser()
  }

  @Test
  fun `resetCurrentUser sets currentUser to null`() {
    val user = User("10000", "Test User", "Test", "User", "test@example.com", null)
    User.setCurrentUser(user)
    User.resetCurrentUser()
    assertNull(User.getCurrentUser())
  }

  @Test
  fun `User creation with null values`() {
    val user = User("1", null, null, null, "test@gmail.com", null)

    assertNull(user.displayName)
    assertNull(user.familyName)
    assertNull(user.givenName)

    assertNull(user.photoURL)
  }

  @Test
  fun `User creation with empty strings`() {
    val user = User("", "", "", "", "", null)
    assertEquals("", user.id)
    assertEquals("", user.displayName)
    assertEquals("", user.familyName)
    assertEquals("", user.givenName)
    assertEquals("", user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `User creation with long strings`() {
    val longString = "a".repeat(1000)
    val user = User(longString, longString, longString, longString, longString, null)
    assertEquals(longString, user.id)
    assertEquals(longString, user.displayName)
    assertEquals(longString, user.familyName)
    assertEquals(longString, user.givenName)
    assertEquals(longString, user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `User creation with special characters`() {
    val specialString = "!@#$%^&*()_+"
    val user = User(specialString, specialString, specialString, specialString, specialString, null)
    assertEquals(specialString, user.id)
    assertEquals(specialString, user.displayName)
    assertEquals(specialString, user.familyName)
    assertEquals(specialString, user.givenName)
    assertEquals(specialString, user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `can be set once`() {
    val user = User("1", "Test User", "Test", "User", "test@example.com", null)
    val user2 = User("2", "Test User2", "Test2", "User2", "test2@example.com", null)

    User.setCurrentUser(user)
    println(User.getCurrentUser()) // Expected to print user details

    try {
      User.setCurrentUser(user2)
      println(User.getCurrentUser()) // Not expected to run
    } catch (e: IllegalStateException) {
      println(e.message) // Expected to handle exception
    }

    assertEquals(
        user, User.getCurrentUser()) // Verify that the first user is still the current user
    User.resetCurrentUser() // Reset the user for cleanup
  }

  @Test
  fun `set reset set`() {
    val user = User("1", "Test User", "Test", "User", "test@example.com", null)
    val user2 = User("2", "Test User2", "Test2", "User2", "test2@example.com", null)
    User.setCurrentUser(user)
    assertEquals(user, User.getCurrentUser())
    User.resetCurrentUser()
    assertNull(User.getCurrentUser())
    User.setCurrentUser(user2)
    assertEquals(user2, User.getCurrentUser())
    User.resetCurrentUser()
  }
}
