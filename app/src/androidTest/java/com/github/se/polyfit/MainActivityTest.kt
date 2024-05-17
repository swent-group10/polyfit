package com.github.se.polyfit

import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.utils.Authentication
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import java.lang.Thread.sleep
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
  fun testDirectSignIn() {
    val auth: FirebaseAuth = mockk(relaxed = true)
    every { auth.currentUser } answers { mockk(relaxed = true) }

    val scenario = ActivityScenario.launch(MainActivity::class.java)

    scenario.onActivity { activity ->
      val authentication =
          Authentication(activity, mockk(relaxed = true), mockk(relaxed = true), auth)
      authentication.setCallback({}, 0)
      var i = 0
      while (!authentication.isAnswered()) {
        sleep(10)
        Log.i("MainActivityTest", "Waiting for authentication")
        i++
        assert(i < 10_000)
      }
    }
  }

  @Test
  fun testSignOut() {
    val auth: FirebaseAuth = mockk(relaxed = true)
    every { auth.currentUser } answers { mockk(relaxed = true) }

    val scenario = ActivityScenario.launch(MainActivity::class.java)

    scenario.onActivity { activity ->
      val authentication =
          Authentication(activity, mockk(relaxed = true), mockk(relaxed = true), auth)
      authentication.setCallback({}, 0)
      var i = 0
      while (!authentication.isAnswered()) {
        sleep(10)
        Log.i("MainActivityTest", "Waiting for authentication")
        i++
        assert(i < 10_000)
      }
      authentication.signOut()
    }
  }
}
