package com.github.se.polyfit.viewmodel.settings

import com.github.se.polyfit.data.remote.firebase.UserFirebaseRepository
import com.github.se.polyfit.model.data.User
import com.google.android.gms.tasks.Tasks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import org.junit.Before
import org.junit.Test

class AccountSettingsViewModelTest {
  private lateinit var viewModel: AccountSettingsViewModel
  private val user = User("1", "Joe", "S", "Joe", "a@a")
  private val mockUserFirebaseRepository = mockk<UserFirebaseRepository>(relaxed = true)

  @Before
  fun setup() {
    viewModel = AccountSettingsViewModel(user, mockUserFirebaseRepository)
  }

  @Test
  fun updateDisplayName() {
    viewModel.updateDisplayName("Jay")
    assert(viewModel.user.value.displayName == "Jay")
  }

  @Test
  fun updateFirstName() {
    viewModel.updateFirstName("Jay")
    assert(viewModel.user.value.givenName == "Jay")
  }

  @Test
  fun updateLastName() {
    viewModel.updateLastName("Jay")
    assert(viewModel.user.value.familyName == "Jay")
  }

  @Test
  fun updateHeightWithValue() {
    viewModel.updateHeight(180L)
    assert(viewModel.user.value.heightCm == 180L)
  }

  @Test
  fun updateHeightWithNull() {
    viewModel.updateHeight(null)
    assert(viewModel.user.value.heightCm == null)
  }

  @Test
  fun updateWeightWithValue() {
    viewModel.updateWeight(80.0)
    assert(viewModel.user.value.weightKg == 80.0)
  }

  @Test
  fun updateWeightWithNull() {
    viewModel.updateWeight(null)
    assert(viewModel.user.value.weightKg == null)
  }

  @Test
  fun updateCalorieGoal() {
    viewModel.updateCalorieGoal(2000L)
    assert(viewModel.user.value.calorieGoal == 2000L)
  }

  @Test
  fun updateDob() {
    val date = LocalDate.of(2021, 1, 1)
    viewModel.updateDob(date)
    assert(viewModel.user.value.dob == date)
  }

  @Test
  fun updateIsVegetarian() {
    viewModel.updateIsVegetarian(true)
    assert(viewModel.user.value.isVegetarian)
  }

  @Test
  fun updateIsVegan() {
    viewModel.updateIsVegan(true)
    assert(viewModel.user.value.isVegan)
  }

  @Test
  fun updateIsGlutenFree() {
    viewModel.updateIsGlutenFree(true)
    assert(viewModel.user.value.isGlutenFree)
  }

  @Test
  fun updateIsLactoseFree() {
    viewModel.updateIsLactoseFree(true)
    assert(viewModel.user.value.isLactoseFree)
  }

  @Test
  fun updateUserChangesInputAndCallsFirebase() {
    coEvery { mockUserFirebaseRepository.storeUser(any()) } returns Tasks.forResult(null)

    viewModel.updateDisplayName("Jay")
    viewModel.updateFirstName("Jay")
    viewModel.updateLastName("Jay")
    viewModel.updateHeight(180L)
    viewModel.updateWeight(80.0)
    viewModel.updateCalorieGoal(2000L)
    val date = LocalDate.of(2021, 1, 1)
    viewModel.updateDob(date)
    viewModel.updateIsVegetarian(true)
    viewModel.updateIsVegan(true)
    viewModel.updateIsGlutenFree(true)
    viewModel.updateIsLactoseFree(true)
    viewModel.updateUser()
    assert(user.displayName == "Jay")
    assert(user.givenName == "Jay")
    assert(user.familyName == "Jay")
    assert(user.heightCm == 180L)
    assert(user.weightKg == 80.0)
    assert(user.calorieGoal == 2000L)
    assert(user.dob == date)
    assert(user.isVegetarian)
    assert(user.isVegan)
    assert(user.isGlutenFree)
    assert(user.isLactoseFree)

    verify { mockUserFirebaseRepository.storeUser(viewModel.user.value) }
  }
}
