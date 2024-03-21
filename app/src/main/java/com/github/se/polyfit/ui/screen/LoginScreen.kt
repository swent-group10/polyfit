package com.github.se.polyfit.ui.screen

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.LoginViewModel
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.navigation.NavigationInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject


@Composable
fun LoginScreen(goTo : () -> Unit) {

    // Create an instance of the Authentication class

    val viewModel: LoginViewModel = hiltViewModel()
    //val authentication = AuthenticationCloud(context = LocalContext.current){if (it) navController.navigateToHome()}
    val signInLauncher =
        rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) { res ->
            viewModel.onSignInResult(res){if(it) goTo()}
        }

    // Set the signInLauncher in the Authentication class
    viewModel.setSignInLauncher(signInLauncher)

    // This function starts the sign-in process
    fun createSignInIntent() {
        viewModel.signIn()
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("LoginScreen"),
        color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(Modifier.weight(0.15f))

            Text(
                text = "Welcome",
                fontSize = 30.sp,
                modifier = Modifier.testTag("LoginTitle"),
            )

            Spacer(Modifier.weight(0.5f))

            OutlinedButton(
                onClick = {
                    Log.d("LoginScreen", "button clicked")
                    createSignInIntent()
                },
                shape = RoundedCornerShape(20.dp),
                colors =
                ButtonDefaults.buttonColors(
                    contentColor = Color.Black, containerColor = Color.White),
                modifier = Modifier.testTag("LoginButton")) {
                val imageModifierGoogle = Modifier.size(24.dp)

                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google_logo",
                    contentScale = ContentScale.Fit,
                    modifier = imageModifierGoogle)
                Text(text = "Sign in with Google", modifier = Modifier.padding(start = 16.dp))
            }
            Spacer(Modifier.weight(0.3f))
        }
    }
}
