package com.github.se.polyfit.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.model.data.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface Authentication {
  fun signIn()

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback: (Boolean) -> Unit)

  fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>)
}

@HiltViewModel
class AuthenticationCloud
@Inject
constructor(private val context: Context, private val user: User) : ViewModel(), Authentication {
  private lateinit var signInLauncher: ActivityResultLauncher<Intent>

  override fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
    signInLauncher = launcher
  }

  override fun signIn() {
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

    val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

    val signInIntent = mGoogleSignInClient.signInIntent
    signInLauncher.launch(signInIntent)
  }

  override fun onSignInResult(
      result: FirebaseAuthUIAuthenticationResult,
      callback: (Boolean) -> Unit
  ) {
    Log.i("LoginScreen", "onSignInResult")
    val response = result.idpResponse
    Log.i("LoginScreen", "response: $response")
    if (result.resultCode == Activity.RESULT_OK) {
      Log.i("LoginScreen", "User signed in")
      // to get google acount infos
      val account = GoogleSignIn.getLastSignedInAccount(context)

      this.user.id = account?.id!!
      this.user.displayName = account.displayName ?: ""
      this.user.familyName = account.familyName ?: ""
      this.user.givenName = account.givenName ?: ""
      this.user.email = account.email!!
      this.user.photoURL = account.photoUrl

      callback(true)
    } else {
      Log.e("LoginScreen", "Error in result: ${result.resultCode}")
      response?.let {
        Log.e("LoginScreen", "Error in result firebase authentication: " + "${it.error?.errorCode}")
      }
      callback(false)
    }
  }
}
