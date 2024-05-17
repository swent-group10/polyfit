package com.github.se.polyfit.ui.utils

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import com.github.se.polyfit.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import org.junit.Test

class AuthenticationTest {

  private val user: User = User()
  private lateinit var activity: ComponentActivity
  private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var firebaseuser: FirebaseUser
  private lateinit var authentication: Authentication
  private lateinit var context: Context

  @BeforeTest
  fun setup() {
    mockkStatic(Log::class)

    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0

    activity = mockk(relaxed = true)
    firebaseAuth = mockk(relaxed = true)
    firebaseuser = mockk<FirebaseUser>(relaxed = true)

    context = ApplicationProvider.getApplicationContext<Context>()
  }

  @AfterTest
  fun after() {
    unmockkAll()
  }

  @Test
  fun isAuthenticated() {
    var isLoggedIn = false
    every { firebaseAuth.currentUser } answers
        {
          if (isLoggedIn) {
            firebaseuser
          } else {
            isLoggedIn = true
            null
          }
        }
    authentication = Authentication(activity, user, mockk(), firebaseAuth, context)
    authentication.setCallback({}, 3)
    assert(authentication.isAuthenticated())
  }

  @Test
  fun signOut() {
    every { firebaseAuth.currentUser } returns null

    authentication = Authentication(activity, user, mockk(), firebaseAuth, context)
    authentication.setCallback({}, 3)
    authentication.signOut()
    assert(!authentication.isAuthenticated())
  }
}
