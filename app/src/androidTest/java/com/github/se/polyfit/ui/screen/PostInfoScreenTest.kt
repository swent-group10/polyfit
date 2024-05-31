package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.viewmodel.post.ViewPostViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule

class PostInfoScreenTest {
  @get:Rule val composeTestRule = createComposeRule()

  private val mockNavigation = mockk<Navigation>(relaxed = true)
  private val mockNavController = mockk<NavHostController>(relaxed = true)
  private val mockPostViewModel = mockk<ViewPostViewModel>(relaxed = true)

  @Before
  fun setup() {
    every { mockPostViewModel.isFetching } returns MutableLiveData(true)

    composeTestRule.setContent {
      PostInfoScreen(
        navigation = mockNavigation,
        navHostController = mockNavController,
        viewPostViewModel = mockPostViewModel)
    }
  }

  @Test
  fun assertEverythingIsDisplayed() {

    ComposeScreen.onComposeScreen<PostInfoScreenScreen>(composeTestRule) {
      assertIsDisplayed()
      composeTestRule.onNodeWithTag("LoadingPost").assertIsDisplayed()
      composeTestRule.onNodeWithTag("MainBottomBar").assertIsDisplayed()
    }
  }
}
