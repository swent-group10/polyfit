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

  @Test
  fun `User creation with null values`() {
    val user = User(null, null, null, null, null, null)
    assertNull(user.id)
    assertNull(user.displayName)
    assertNull(user.familyName)
    assertNull(user.getGivenName)
    assertNull(user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `User creation with empty strings`() {
    val user = User("", "", "", "", "", null)
    assertEquals("", user.id)
    assertEquals("", user.displayName)
    assertEquals("", user.familyName)
    assertEquals("", user.getGivenName)
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
    assertEquals(longString, user.getGivenName)
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
    assertEquals(specialString, user.getGivenName)
    assertEquals(specialString, user.email)
    assertNull(user.photoURL)
  }
}
