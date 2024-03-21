package com.github.se.polyfit

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.ui.navigation.NavigationInterface
import com.github.se.polyfit.ui.screen.Authentication
import com.github.se.polyfit.ui.screen.MockAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel
class LoginViewModel (
    private val authentication: Authentication
) : ViewModel() {

    fun signIn() {
        authentication.signIn()
    }

    fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
        authentication.setSignInLauncher(launcher)
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        authentication.onSignInResult(result)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    /*@Provides
    fun provideAuthentication(goTo : () -> Unit ): Authentication {
        return MockAuthentication { isSuccess ->
            if (isSuccess) {
                goTo()
            }
        }
    }*/
}