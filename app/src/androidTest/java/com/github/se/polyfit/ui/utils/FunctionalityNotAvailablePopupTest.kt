import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.components.FunctionalityNotAvailablePopup
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FunctionalityNotAvailablePopupTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun simpleComposable_showsPopupWhenButtonClicked() {
    composeTestRule.setContent { SimpleComposable() }

    composeTestRule.onNodeWithText("Functionality not available \uD83D\uDE48").assertDoesNotExist()

    composeTestRule.onNodeWithText("Button Text").performClick()

    composeTestRule.onNodeWithText("Functionality not available \uD83D\uDE48").assertExists()
  }

  @Test
  fun simpleComposable_dismissesPopupWhenCloseClicked() {
    composeTestRule.setContent { SimpleComposable() }

    composeTestRule.onNodeWithText("Button Text").performClick()

    composeTestRule.onNodeWithText("Close").performClick()

    composeTestRule.onNodeWithText("Functionality not available \uD83D\uDE48").assertDoesNotExist()
  }
}

@Composable
fun SimpleComposable() {
  var notAvailablePopup by remember { mutableStateOf(false) }
  if (notAvailablePopup) {
    FunctionalityNotAvailablePopup { notAvailablePopup = false }
  }
  Button(onClick = { notAvailablePopup = true }, content = { Text("Button Text") })
}
