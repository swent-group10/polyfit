package com.github.se.polyfit.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.remote.firebase.UserFirebaseRepository
import com.github.se.polyfit.model.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AccountSettingsViewModel
@Inject
constructor(
    private val currentUser: User,
    private val userFirebaseRepository: UserFirebaseRepository
) : ViewModel() {
  private val _user = MutableStateFlow(currentUser)
  val user: StateFlow<User> = _user

  fun updateDisplayName(name: String) {
    _user.value = _user.value.copy(displayName = name)
  }

  fun updateFirstName(name: String) {
    _user.value = _user.value.copy(givenName = name)
  }

  fun updateLastName(name: String) {
    _user.value = _user.value.copy(familyName = name)
  }

  fun updateHeight(height: Long?) {
    _user.value = _user.value.copy(heightCm = height)
  }

  fun updateWeight(weight: Double?) {
    _user.value = _user.value.copy(weightKg = weight)
  }

  fun updateCalorieGoal(calorieGoal: Long) {
    _user.value = _user.value.copy(calorieGoal = calorieGoal)
  }

  fun updateDob(date: LocalDate) {
    _user.value = _user.value.copy(dob = date)
  }

  fun updateIsVegetarian(isVegetarian: Boolean) {
    _user.value = _user.value.copy(isVegetarian = isVegetarian)
  }

  fun updateIsVegan(isVegan: Boolean) {
    _user.value = _user.value.copy(isVegan = isVegan)
  }

  fun updateIsGlutenFree(isGlutenFree: Boolean) {
    _user.value = _user.value.copy(isGlutenFree = isGlutenFree)
  }

  fun updateIsLactoseFree(isLactoseFree: Boolean) {
    _user.value = _user.value.copy(isLactoseFree = isLactoseFree)
  }

  fun updateUser() {
    viewModelScope.launch {
      withContext(Dispatchers.IO) { userFirebaseRepository.storeUser(_user.value) }
    }
    currentUser.update(_user.value)
  }
}
