package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraXTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun displays_cameraX() {
    composeTestRule.setContent { CameraXScreen(BarCodeCodeViewModel()) }

    ComposeScreen.onComposeScreen<CameraXScreen>(composeTestRule) {
      AndroidView {
        assertExists()
        assertIsDisplayed()
      }
    }
  }
}