package com.github.se.polyfit

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.ui.screen.Authentication
import com.github.se.polyfit.ui.screen.AuthenticationCloud
import com.github.se.polyfit.ui.screen.MockAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authentication: Authentication
) : ViewModel() {

    fun signIn(){
        authentication.signIn()
    }

    fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
        authentication.setSignInLauncher(launcher)
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, callback : (Boolean) -> Unit) {
        authentication.onSignInResult(result, callback)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {
    @Provides
    fun provideAuthentication(@ApplicationContext context: Context): Authentication {
        return AuthenticationCloud(context)
    }
}