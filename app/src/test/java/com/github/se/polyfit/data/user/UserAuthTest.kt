package com.github.se.polyfit.data.user

import android.app.Activity
import android.util.Log
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.utils.AuthenticationCloud
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserAuthTest {

  private lateinit var mockAccount: GoogleSignInAccount
  private lateinit var authCloud: AuthenticationCloud

  @BeforeTest
  fun setup() {
    // Mock the Log class
    mockkStatic(Log::class)
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0
    // Create a mock GoogleSignInAccount object
    mockAccount = mockk()
    every { mockAccount.id } returns "1"
    every { mockAccount.displayName } returns "Test User"
    every { mockAccount.familyName } returns "Test"
    every { mockAccount.givenName } returns "User"
    every { mockAccount.email } returns "test@example.com"
    every { mockAccount.photoUrl } returns null

    // Mock the GoogleSignIn.getLastSignedInAccount(context) method
    mockkStatic(GoogleSignIn::class)
    every { GoogleSignIn.getLastSignedInAccount(any()) } returns mockAccount

    // Initialize AuthenticationCloud
    authCloud = AuthenticationCloud(mockk(relaxed = true))
  }

  @AfterTest
  fun teardown() {
    unmockkAll()
  }

  @Test
  fun `User currentUser is set correctly`() {
    // Create a mock FirebaseAuthUIAuthenticationResult
    val mockResult = mockk<FirebaseAuthUIAuthenticationResult>()
    every { mockResult.resultCode } returns Activity.RESULT_OK
    every { mockResult.idpResponse } returns null

    // Call onSignInResult
    authCloud.onSignInResult(mockResult) {}

    // Verify that User.currentUser is set with the correct values
    User.getCurrentUser()?.let { assertEquals("1", it.id) }
    assertEquals("Test User", User.getCurrentUser()?.displayName)
    assertEquals("Test", User.getCurrentUser()?.familyName)
    assertEquals("User", User.getCurrentUser()?.givenName)
    assertEquals("test@example.com", User.getCurrentUser()?.email)
    assertNull(User.getCurrentUser()?.photoURL)
  }
}
