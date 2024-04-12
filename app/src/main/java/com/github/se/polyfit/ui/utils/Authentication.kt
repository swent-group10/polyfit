package com.github.se.polyfit.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.model.data.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject

interface Authentication {
  fun signIn()

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback: (Boolean) -> Unit)

  fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>)
}

class AuthenticationCloud @Inject constructor(private val context: Context) : Authentication {
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

      User.setCurrentUser(
          User(
              account?.id!!,
              account.displayName ?: "",
              account.familyName ?: "",
              account.givenName ?: "",
              account.email!!,
              account.photoUrl))

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
