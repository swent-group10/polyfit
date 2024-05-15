package com.github.se.polyfit

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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

  @JvmField
  @Rule
  var mMainActivityRule: ActivityScenarioRule<MainActivity> =
      ActivityScenarioRule(MainActivity::class.java)

  @Test
  fun testSignInSignOut() {
    val auth: FirebaseAuth = mockk(relaxed = true)
    every { auth.currentUser } answers { mockk(relaxed = true) }

    var authentication: Authentication? = null
    val scenario = ActivityScenario.launch(MainActivity::class.java)

    scenario.onActivity { activity ->
      authentication = Authentication(activity, mockk(relaxed = true), mockk(relaxed = true), auth)
      authentication!!.setCallback({}, 0)
    }

    assert(authentication!!.isAuthenticated())
  }
}
