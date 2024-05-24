package com.github.se.polyfit

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.utils.Authentication
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

  @get:Rule val mockkRule = MockKRule(this)

  @Test
  fun testDirectFirstTimeNotSignedIn() {
    val auth: FirebaseAuth = mockk(relaxed = true)
    every { auth.currentUser } answers { mockk(relaxed = true) }

    val scenario = ActivityScenario.launch(MainActivity::class.java)
    scenario.recreate()
    var user: User? = null
    var authentication: Authentication? = null
    scenario.onActivity { activity ->
      user = activity.user
      authentication = activity.authentication
      authentication!!.setCallbackOnSign {}
    }
    scenario.moveToState(Lifecycle.State.RESUMED)

    assert(!authentication!!.isAuthenticated())
    assert(!user!!.isSignedIn())
    assert(authentication!!.user == user)
  }

  @Test
  fun testSignOut() {
    val auth: FirebaseAuth = mockk(relaxed = true)
    every { auth.currentUser } answers { mockk(relaxed = true) }

    val scenario = ActivityScenario.launch(MainActivity::class.java)
    scenario.moveToState(Lifecycle.State.RESUMED)
    scenario.onActivity { activity ->
      val authentication = activity.authentication
      authentication.setCallbackOnSign {}
      authentication.signIn()
      authentication.signOut()
      assert(!authentication.isAuthenticated())
    }
  }
}
