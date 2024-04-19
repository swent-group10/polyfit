package com.github.se.polyfit

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

  @Test
  fun testActivityShouldBeLaunched() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      // Add your test code here. For example, you can use Espresso to interact with UI elements.
    }
  }
}
