package com.github.se.polyfit.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.R
import com.github.se.polyfit.model.data.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AuthTmp(val context: Context, private val user: User) {

  var callback: (() -> Unit)? = null
  lateinit var activity: ComponentActivity
  lateinit var auth: FirebaseAuth
  private lateinit var signInLauncher: ActivityResultLauncher<Intent>

  fun onCreate(activity: ComponentActivity) {
    // this.callback = callback
    this.activity = activity
    this.auth = FirebaseAuth.getInstance()

    signInLauncher =
        activity.registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
          onSignInResult(res) {
            if (it) {
              var c = callback
              while (c == null) {
                c = callback
                Log.w("AuthTmp", "waiting for callback, should not be stuck here")
              }
              (callback!!)()
              Log.i("AuthTmp", "onCreate user account: ${auth.currentUser}")
            } else {
              Log.d("LoginScreen", "Sign in failed")

              // Display a toast message if the sign-in fails to the user
              Toast.makeText(
                      context,
                      ContextCompat.getString(context, R.string.signInFailed),
                      Toast.LENGTH_SHORT)
                  .show()
            }
          }
        }
    val providers =
        arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    signInLauncher.launch(signInIntent)
  }

  fun setCallback(callback: () -> Unit, asd: Int) {
    this.callback = callback
  }

  fun isAuthenticated(): Boolean {
    Log.i("AuthTmp", "isAuthenticated: ${auth.currentUser}")
    return auth.currentUser != null
  }

  fun signIn() {
    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    signInLauncher.launch(signInIntent)
  }

  fun signOut() {
    AuthUI.getInstance().signOut(context)
    Firebase.auth.currentUser!!.delete()
  }

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback: (Boolean) -> Unit) {
    val response = result.idpResponse
    if (result.resultCode == Activity.RESULT_OK) {
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
