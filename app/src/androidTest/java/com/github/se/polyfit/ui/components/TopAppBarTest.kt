import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.polyfit.ui.components.DefaultDrawerAppBar
import com.github.se.polyfit.ui.components.WithSearchDrawerAppBar
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

  @get:Rule val composeTestRule = createComposeRule()

  @OptIn(ExperimentalMaterial3Api::class)
  @Test
  fun defaultDrawerAppBar_isDisplayed() {
    composeTestRule.setContent { DefaultDrawerAppBar(title = { Text("Test Title") }) }

    composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Test
  fun withSearchDrawerAppBar_isDisplayed() {
    composeTestRule.setContent { WithSearchDrawerAppBar(title = { Text("Test Title") }) }

    composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Test
  fun defaultDrawerAppBar_onNavIconPressed_isCalled() {
    var navIconPressed = false

    composeTestRule.setContent {
      DefaultDrawerAppBar(
          title = { Text("Test Title") }, onNavIconPressed = { navIconPressed = true })
    }

    composeTestRule.onNodeWithContentDescription("App Icon").performClick()

    assert(navIconPressed)
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Test
  fun withSearchDrawerAppBar_onNavIconPressed_isCalled() {
    var navIconPressed = false

    composeTestRule.setContent {
      WithSearchDrawerAppBar(
          title = { Text("Test Title") }, onNavIconPressed = { navIconPressed = true })
    }

    composeTestRule.onNodeWithContentDescription("App Icon").performClick()

    assert(navIconPressed)
  }
}
