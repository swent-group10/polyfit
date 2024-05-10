package com.github.se.polyfit.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Singleton

@Singleton
class Authentication(activity: ComponentActivity,
                     private val user: User,
                      private val auth: FirebaseAuth = FirebaseAuth.getInstance()){

  private var callback: (() -> Unit)? = null
  private var signInLauncher: ActivityResultLauncher<Intent>? = null
  private val context: Context = activity.applicationContext
  init{
    initLaunch(activity)
  }

  private fun initLaunch(activity: ComponentActivity) {
    if(auth.currentUser != null) {
      setUser(GoogleSignIn.getLastSignedInAccount(context))
      return
    }
    // This init is launched when the user is not signed in
    signInLauncher =
            activity.registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
              onSignInResult(res) {
                if (it) {
                  var c = callback
                  while (c == null) {
                    c = callback
                    Log.w("Authentication", "waiting for callback, should not be stuck here")
                  }
                  (callback!!)()
                  Log.i("Authentication", "onCreate user account: ${auth.currentUser}")
                } else {
                  Log.e("LoginScreen", "Sign in failed")

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

    signInLauncher!!.launch(signInIntent)
  }

  fun setCallback(callback: () -> Unit, asd: Int) {
    this.callback = callback
  }

  fun isAuthenticated(): Boolean {
    Log.i("Authentication", "isAuthenticated: ${auth.currentUser}")
    return auth.currentUser != null
  }

  fun signIn() {
    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    signInLauncher!!.launch(signInIntent)
  }

  fun signOut() {
    AuthUI.getInstance().signOut(context)
  }

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback: (Boolean) -> Unit) {
    val response = result.idpResponse
    if (result.resultCode == Activity.RESULT_OK) {
      // to get google acount infos
      val account = GoogleSignIn.getLastSignedInAccount(context)

      setUser(account)

      callback(true)
    } else {
      Log.e("LoginScreen", "Error in result: ${result.resultCode}")
      response?.let {
        Log.e("LoginScreen", "Error in result firebase authentication: " + "${it.error?.errorCode}")
      }
      callback(false)
    }
  }

  private fun setUser(account: GoogleSignInAccount?) {
    this.user.id = account?.id!!
    this.user.displayName = account.displayName ?: ""
    this.user.familyName = account.familyName ?: ""
    this.user.givenName = account.givenName ?: ""
    this.user.email = account.email!!
    this.user.photoURL = account.photoUrl
  }
}
