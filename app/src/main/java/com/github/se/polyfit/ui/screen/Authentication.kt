package com.github.se.polyfit.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.ui.navigation.NavigationInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

interface Authentication {
    fun signIn()
    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult)

    fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>)

}


@Module
@InstallIn(ActivityComponent::class)
object AuthModule {

    @Provides
    fun provideAuthentication(
        goTo : () -> Unit
    ): Authentication {
        // Adjust the constructor as needed to fit your implementation
        return MockAuthentication { isSuccess ->
            if (isSuccess) {
                goTo()
            }
        }
    }
}

class MockAuthentication @Inject constructor(private val callback: (Boolean) -> Unit) : Authentication {
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
        signInLauncher = launcher
    }

    override fun signIn() {
        callback(true)
    }

    override fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        callback(true)
    }
}

class AuthenticationCloud @Inject constructor(
    private val context: Context,
    private val callback: (Boolean) -> Unit
) : Authentication {
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

    override fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        Log.i("LoginScreen", "onSignInResult")
        val response = result.idpResponse
        Log.i("LoginScreen", "response: $response")
        if (result.resultCode == Activity.RESULT_OK) {
            Log.i("LoginScreen", "User signed in")
            callback(true)
        } else {
            Log.e("LoginScreen", "Error in result: ${result.resultCode}")
            response?.let { Log.e("LoginScreen", "Error in result firebase authentication: " +
                    "${it.error?.errorCode}") }
            callback(false)
        }
    }
}