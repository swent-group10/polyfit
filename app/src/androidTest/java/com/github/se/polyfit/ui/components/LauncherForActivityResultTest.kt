package com.github.se.polyfit.ui.components

import android.Manifest
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.app.ActivityOptionsCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.components.dialog.launcherForActivityResult
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LauncherForActivityResultTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testLauncherForActivityResult_onApprove() {
    val onApprove = mockk<() -> Unit>(relaxed = true)
    val onDeny = mockk<() -> Unit>(relaxed = true)

    val registryOwner =
        object : ActivityResultRegistryOwner {
          private val _registry =
              object : ActivityResultRegistry() {
                override fun <I, O> onLaunch(
                    requestCode: Int,
                    contract: ActivityResultContract<I, O>,
                    input: I,
                    options: ActivityOptionsCompat?
                ) {
                  dispatchResult(requestCode, true)
                }
              }
          override val activityResultRegistry: ActivityResultRegistry
            get() = _registry
        }

    composeTestRule.setContent {
      CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
        TestableLauncherForActivityResult(onDeny, onApprove)
      }
    }
    composeTestRule.onNodeWithText("Request Permission").performClick()
    verify { onApprove.invoke() }
  }

  @Test
  fun testLauncherForActivityResult_onDeny() {
    val onApprove = mockk<() -> Unit>(relaxed = true)
    val onDeny = mockk<() -> Unit>(relaxed = true)

    val registryOwner =
        object : ActivityResultRegistryOwner {
          private val _registry =
              object : ActivityResultRegistry() {
                override fun <I, O> onLaunch(
                    requestCode: Int,
                    contract: ActivityResultContract<I, O>,
                    input: I,
                    options: ActivityOptionsCompat?
                ) {
                  dispatchResult(requestCode, false)
                }
              }
          override val activityResultRegistry: ActivityResultRegistry
            get() = _registry
        }

    composeTestRule.setContent {
      CompositionLocalProvider(LocalActivityResultRegistryOwner provides registryOwner) {
        TestableLauncherForActivityResult(onDeny, onApprove)
      }
    }
    composeTestRule.onNodeWithText("Request Permission").performClick()

    verify { onDeny.invoke() }
  }

  @Composable
  fun TestableLauncherForActivityResult(onDeny: () -> Unit, onApprove: () -> Unit) {
    val launcher = launcherForActivityResult(onDeny, onApprove)
    Button(onClick = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
      Text("Request Permission")
    }
  }
}
