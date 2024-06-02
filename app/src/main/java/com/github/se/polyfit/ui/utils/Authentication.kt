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
    val user: User,
    private val userFirebaseRepository: UserFirebaseRepository,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private var context: Context = activity.applicationContext,
) {

  private var callback: (() -> Unit) = { throw Throwable("Callback not set") }
  private var signInLauncher: ActivityResultLauncher<Intent>
  // This value is used to be sure the request is received in the test.
  private var isAnswered = false

  init {
    Log.v("Authentication", "Authentication initialized")
    signInLauncher =
        activity.registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
          onSignInResult(res)
        }
    if (firebaseAuth.currentUser != null) {
      Log.v("Authentication", "User already authenticated")
      setUserInfo(GoogleSignIn.getLastSignedInAccount(context))
      isAnswered = true
    }
    Log.v("Authentication", "End of Authentication initialized")
  }

  fun setCallbackOnSign(callback: () -> Unit) {
    Log.v("Authentication", "Callback set")
    this.callback = callback
  }

  fun isAuthenticated(): Boolean {
    val result = firebaseAuth.currentUser != null
    Log.i("Authentication", "isAuthenticated: $result")
    return result
  }

  fun signIn() {
    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    signInLauncher.launch(signInIntent)
    Log.i("Authentication", "Sign in launched")
  }

  fun signOut() {
    AuthUI.getInstance().signOut(context)
    user.signOut()
    isAnswered = false
    Log.v("Authentication", "Sign out")
  }

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
    Log.i("Authentication", "Sign in result received")
    val response = result.idpResponse
    if (result.resultCode == Activity.RESULT_OK) {
      // to get google account info
      Log.i("Authentication", "Sign in successful $response")
      val account = GoogleSignIn.getLastSignedInAccount(context)
      setUserInfo(account)

      callback()
    } else {
      Log.e("Authentication", "Error in result: ${result.resultCode}")
    }
    isAnswered = true
  }

  private fun setUserInfo(account: GoogleSignInAccount?) {
    this.user.update(
        id = account!!.id!!,
        email = account.email!!,
        displayName = account.displayName,
        familyName = account.familyName,
        givenName = account.givenName,
        photoURL = account.photoUrl)

    userFirebaseRepository.getUser(this.user.id).continueWith {
      if (it.result.isNotNull()) {
        Log.d("Authentication", "User already exists, information loaded")
        this.user.update(user = it.result!!)
      } else {
        userFirebaseRepository.storeUser(this.user)
      }
    }
  }
}
