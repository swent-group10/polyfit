package com.github.se.polyfit

import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
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

  @Test
  fun testSignInSignOut() {
    val auth: FirebaseAuth = mockk(relaxed = true)
    every { auth.currentUser } answers { mockk(relaxed = true) }

    val scenario = ActivityScenario.launch(MainActivity::class.java)
    scenario.recreate()
    scenario.onActivity { activity ->
      val authentication = activity.authentication
      authentication.setCallbackOnSign {}
      authentication.signIn()
      var i = 0
      while (!authentication.isAnswered()) {
        sleep(10)
        Log.i("MainActivityTest", "Waiting for authentication")
        i++
        assert(i < 1_000)
      }
      assert(authentication.isAuthenticated())
      authentication.signOut()
      assert(!authentication.isAuthenticated())
    }
  }
}
