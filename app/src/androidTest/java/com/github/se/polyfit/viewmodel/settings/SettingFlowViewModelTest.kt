package com.github.se.polyfit.viewmodel.settings

import com.github.se.polyfit.model.data.User
import com.google.firebase.auth.FirebaseAuth
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SettingFlowViewModelTest {
  private lateinit var viewModel: SettingFlowViewModel
  private lateinit var user: User
  private val mockFirebaseAuth: FirebaseAuth = mockk(relaxed = true)

  @Before
  fun setup() {
    user = User("1", "Joe", "S", "Joe", "a@a")
    viewModel = SettingFlowViewModel(user, mockFirebaseAuth)
  }

  @Test
  fun signOut() {
    viewModel.signOut()

    assert(user == User())
    verify { mockFirebaseAuth.signOut() }
  }
}
