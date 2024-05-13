package com.github.se.polyfit

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.utils.Authentication
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

  @get:Rule
  val mockkRule = MockKRule(this)

  @JvmField @Rule var globalTimeout: Timeout = Timeout(50, TimeUnit.SECONDS)

  @JvmField
  @Rule
  var mMainActivityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(
          MainActivity::class.java)
  @Test fun testUserID() {
    val scenario = mMainActivityRule.scenario

    scenario.onActivity { activity ->
      Log.i("ADS", "ADSdsa ${activity.user}")
      activity.user
      assert(!activity.user.isSignedIn())
      assert(activity.user.id == "testUserID")
    }
  }
}
