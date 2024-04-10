package com.github.se.polyfit.viewmodel.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.ui.utils.Authentication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authentication: Authentication) : ViewModel() {

  fun signIn() {
    authentication.signIn()
  }

  fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
    authentication.setSignInLauncher(launcher)
  }

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback: (Boolean) -> Unit) {
    authentication.onSignInResult(result, callback)
  }
}
