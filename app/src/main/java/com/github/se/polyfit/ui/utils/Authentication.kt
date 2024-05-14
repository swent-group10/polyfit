package com.github.se.polyfit.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import co.yml.charts.common.extensions.isNotNull
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.data.remote.firebase.UserFirebaseRepository
import com.github.se.polyfit.model.data.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

interface Authentication {
  fun signIn()

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback: (Boolean) -> Unit)

  fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>)
}

@HiltViewModel
class AuthenticationCloud
@Inject
constructor(
    private val context: Context,
    private val user: User,
    private val userFirebaseRepository: UserFirebaseRepository
) : ViewModel(), Authentication {
  private lateinit var signInLauncher: ActivityResultLauncher<Intent>

  override fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
    signInLauncher = launcher
  }

  override fun signIn() {
    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    signInLauncher.launch(signInIntent)
  }

  override fun onSignInResult(
      result: FirebaseAuthUIAuthenticationResult,
      callback: (Boolean) -> Unit
  ) {
    val response = result.idpResponse
    if (result.resultCode == Activity.RESULT_OK) {
      // to get google acount infos
      val account = GoogleSignIn.getLastSignedInAccount(context)
      runBlocking { setUserInfo(account) }

      callback(true)
    } else {
      Log.e("LoginScreen", "Error in result: ${result.resultCode}")
      response?.let {
        Log.e("LoginScreen", "Error in result firebase authentication: " + "${it.error?.errorCode}")
      }
      callback(false)
    }
  }

  private fun setUserInfo(account: GoogleSignInAccount?) {
    this.user.update(
        id = account?.id!!,
        email = account.email!!,
        displayName = account.displayName,
        familyName = account.familyName,
        givenName = account.givenName,
        photoURL = account.photoUrl)

    userFirebaseRepository.getUser(this.user.id).continueWith {
      if (it.result.isNotNull()) {
        Log.d("AuthenticationCloud", "User already exists, information loaded")
        this.user.update(user = it.result!!)
      } else {
        userFirebaseRepository.storeUser(this.user)
      }
    }
  }
}
