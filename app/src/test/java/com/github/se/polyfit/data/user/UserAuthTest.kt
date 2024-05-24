package com.github.se.polyfit.data.user

import android.app.Activity
import android.util.Log
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.utils.Authentication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserAuthTest {

  private lateinit var mockAccount: GoogleSignInAccount
  private lateinit var authentication: Authentication
  private val user: User = User()
  private lateinit var mockFirebaseAuth: FirebaseAuth
  private lateinit var firebaseuser: FirebaseUser

  @BeforeTest
  fun setup() {
    // Mock the Log class
    mockkStatic(Log::class)
    every { Log.v(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0
    // Create a mock GoogleSignInAccount object
    firebaseuser = mockk<FirebaseUser>(relaxed = true)
    mockAccount = mockk()
    mockFirebaseAuth = mockk()
    every { mockAccount.id } returns "1"
    every { mockAccount.displayName } returns "Test User"
    every { mockAccount.familyName } returns "Test"
    every { mockAccount.givenName } returns "User"
    every { mockAccount.email } returns "test@example.com"
    every { mockAccount.photoUrl } returns null
    every { mockFirebaseAuth.currentUser } returns firebaseuser
    every { mockFirebaseAuth.currentUser } returns firebaseuser

    // Mock the GoogleSignIn.getLastSignedInAccount(context) method
    mockkStatic(GoogleSignIn::class)
    every { GoogleSignIn.getLastSignedInAccount(any()) } returns mockAccount

    // Initialize AuthenticationCloud
    authentication =
        Authentication(mockk(relaxed = true), user, mockk(relaxed = true), mockFirebaseAuth)
  }

  @AfterTest
  fun teardown() {
    unmockkAll()
  }

  @Inject
  @Test
  fun `User currentUser is set correctly`() {
    // Create a mock FirebaseAuthUIAuthenticationResult
    val mockResult = mockk<FirebaseAuthUIAuthenticationResult>()
    every { mockResult.resultCode } returns Activity.RESULT_OK
    every { mockResult.idpResponse } returns null

    // Call onSignInResult
    authentication.onSignInResult(mockResult) {}

    // Verify that User.currentUser is set with the correct values
    assertEquals("1", user.id)
    assertEquals("Test User", user.displayName)
    assertEquals("Test", user.familyName)
    assertEquals("User", user.givenName)
    assertEquals("test@example.com", user.email)
    assertEquals(null, user.photoURL)
  }
}
