package com.github.se.polyfit.viewmodel.settings

import androidx.lifecycle.ViewModel
import com.github.se.polyfit.model.data.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingFlowViewModel
@Inject
constructor(private val user: User, private val firebaseInstance: FirebaseAuth) : ViewModel() {
  fun signOut() {
    user.signOut()
    firebaseInstance.signOut()
  }
}
