package com.github.se.polyfit.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import co.yml.charts.common.extensions.isNotNull
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.data.remote.firebase.UserFirebaseRepository
import com.github.se.polyfit.model.data.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth

class Authentication(
    activity: ComponentActivity,
    private val user: User,
    private val userFirebaseRepository: UserFirebaseRepository,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private var context: Context = activity.applicationContext,
) {

  private var callback: (() -> Unit) = { Log.e("Authentication", "Callback not set") }
  private var signInLauncher: ActivityResultLauncher<Intent>
  // This value is used to be sure the request is received in the test.
  private var isAnswered = false

  init {
    signInLauncher =
            activity.registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
              onSignInResult(res) { if (it) (callback)() }
            }
    if (firebaseAuth.currentUser != null) {
      setUserInfo(GoogleSignIn.getLastSignedInAccount(context))
      isAnswered = true
    }
  }

  fun setCallbackOnSign(callback: () -> Unit) {
    this.callback = callback
  }

  fun isAuthenticated(): Boolean {
    return firebaseAuth.currentUser != null || user.isSignedIn()
  }

  fun signIn() {
    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    signInLauncher.launch(signInIntent)
  }

  fun signOut() {
    AuthUI.getInstance().signOut(context)
    user.signOut()
    isAnswered = false
  }

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback: (Boolean) -> Unit) {
    val response = result.idpResponse
    if (result.resultCode == Activity.RESULT_OK) {
      // to get google account info
      val account = GoogleSignIn.getLastSignedInAccount(context)

      setUserInfo(account)

      callback(true)
    } else {
      Log.e("LoginScreen", "Error in result: ${result.resultCode}")
      response?.let {
        Log.e("LoginScreen", "Error in result firebase authentication: " + "${it.error?.errorCode}")
      }
      callback(false)
    }
    isAnswered = true
  }

  private fun setUserInfo(account: GoogleSignInAccount?) {
    if (account == null) {
      Log.e("AuthenticationCloud", "Account is null")
      return
    }
    this.user.update(
        id = account.id!!,
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

  fun isAnswered(): Boolean {
    Log.i("Authentication", "Authentication isAnswered: $isAnswered")
    return isAnswered
  }
}
