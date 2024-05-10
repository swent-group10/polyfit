package com.github.se.polyfit.data.user

import android.net.Uri
import android.util.Log
import com.github.se.polyfit.model.data.User
import io.mockk.every
import io.mockk.mockkStatic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import org.junit.Before

class UserTest {

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any(), any()) } returns 0

    mockkStatic(Uri::class)
    every { Uri.parse(any()) } returns Uri.EMPTY
  }

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

  @Test
  fun `User reset`() {
    val user = User("1", "Test User", "Test", "User", " invalid email", null)
    user.signOut()
    assertEquals("", user.id)
    assertNull(user.displayName)
    assertNull(user.familyName)
  }

  @Test
  fun `User is signed in`() {
    val user = User("1", "Test User", "Test", "User", " invalid email", null)
    assertEquals(true, user.isSignedIn())
  }

  @Test
  fun `User serialization`() {
    val user = User("1", "Test User", "Test", "User", " invalid email", null)
    val map = User.serialize(user)
    assertEquals("1", map["id"])
    assertEquals("Test User", map["displayName"])
    assertEquals("Test", map["familyName"])
    assertEquals("User", map["givenName"])
    assertEquals(" invalid email", map["email"])
    assertEquals("null", map["photoURL"])
  }

  @Test
  fun `User serialization function`() {
    val user = User("1", "Test User", "Test", "User", " invalid email", null)
    val map = user.serialize()
    assertEquals("1", map["id"])
    assertEquals("Test User", map["displayName"])
    assertEquals("Test", map["familyName"])
    assertEquals("User", map["givenName"])
    assertEquals(" invalid email", map["email"])
    assertEquals("null", map["photoURL"])
  }

  @Test
  fun `User deserialization`() {
    val map =
        mapOf(
            "id" to "1",
            "displayName" to "Test User",
            "familyName" to "Test",
            "givenName" to "User",
            "email" to " invalid email",
            "photoURL" to "null")
    val user = User.deserialize(map)
    assertEquals("1", user.id)
    assertEquals("Test User", user.displayName)
    assertEquals("Test", user.familyName)
    assertEquals("User", user.givenName)
    assertEquals(" invalid email", user.email)
    assertNull(user.photoURL)
  }

  @Test
  fun `Update user`() {
    val user = User("1", "Test User", "Test", "User", " invalid email", null)
    user.update("2", "Test User 2", "Test 2", "User 2", " invalid email 2", null)
    assertEquals("2", user.id)
    assertEquals("Test User 2", user.displayName)
    assertEquals("Test 2", user.familyName)
    assertEquals("User 2", user.givenName)
    assertEquals(" invalid email 2", user.email)
    assertNull(user.photoURL)

    user.update(id = "3")
    assertEquals("3", user.id)
    user.update(displayName = "Test User 3")
    assertEquals("Test User 3", user.displayName)
  }

  @Test
  fun `Update user with user object`() {
    val user = User("1", "Test User", "Test", "User", " invalid email", null)
    val newUser = User("2", "Test User 2", "Test 2", "User 2", " invalid email 2", null)
    user.update(newUser)
    assertEquals(user, newUser)
  }

  @Test
  fun `User deserialization with invalid map`() {
    val map =
        mapOf(
            "id" to "null",
            "familyName" to "Test",
            "givenName" to "User",
            "email" to " invalid email",
            "photoURL" to "null")
    assertFailsWith<Exception> { User.deserialize(map) }
  }

  @Test
  fun `User serialize and deserialize remains unchanged`() {
    val user = User("1", "Test User", "Test", "User", " invalid email", null)
    val map = User.serialize(user)
    val deserializedUser = User.deserialize(map)
    assertEquals(user, deserializedUser)
  }
}
