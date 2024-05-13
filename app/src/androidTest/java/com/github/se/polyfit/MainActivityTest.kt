package com.github.se.polyfit

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.utils.Authentication
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Ignore
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
  fun testUserID() {
    val scenario = mMainActivityRule.scenario

    scenario.onActivity { activity ->
      activity.user
      assert(!activity.user.isSignedIn())
      assert(activity.user.id == "testUserID")
    }
  }

  @Ignore("Test not implemented")
  @Test
  fun testEvent() {
    val scenario = mMainActivityRule.scenario

    scenario.moveToState(Lifecycle.State.CREATED)

    scenario.onActivity { activity ->
      val authentication = Authentication(activity, mockk(relaxed = true))
      Log.i("Test", "1")
      authentication.signIn()
      Log.i("Test", "2")
      assert(authentication.isAuthenticated())
      Log.i("Test", "3")
      authentication.signOut()
      Log.i("Test", "4")
      assert(!authentication.isAuthenticated())
      Log.i("Test", "5")
    }

    Log.i("Test", "6")
    scenario.moveToState(Lifecycle.State.STARTED)
    Log.i("Test", "7")
  }
}
