package com.github.se.polyfit.ui.screen

import java.io.File
import kotlin.test.assertTrue
import org.junit.Assert.*
import org.junit.Test

class AuthenticationTest {
  @Test
  fun `google-services json file exists`() {
    // Path to the file relative to the project directory
    val filePath = "google-services.json"
    val file = File(filePath)

    // Assert that the file exists
    assertTrue(
        file.exists(),
        "You should have a google-services.json file" +
            " in the folder app of your project. You can download it from the telegram group.")
  }
}
