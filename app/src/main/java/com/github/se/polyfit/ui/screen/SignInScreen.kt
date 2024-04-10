package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.compose.Title
import com.github.se.polyfit.ui.viewModel.LoginViewModel

@Composable
fun SignScreen(goTo: () -> Unit) {
  // Create an instance of the Authentication class

  val viewModel: LoginViewModel = hiltViewModel()

  val signInLauncher =
      rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) { res ->
        viewModel.onSignInResult(res) { if (it) goTo() else Log.d("LoginScreen", "Sign in failed") }
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
              Spacer(Modifier.weight(0.2f))

              Title(modifier = Modifier.testTag("LoginTitle"), "Polyfit")

              Spacer(Modifier.weight(0.6f))

              SignInButton {
                Log.d("LoginScreen", "button clicked")
                createSignInIntent()
              }

              Spacer(Modifier.weight(0.03f))

              Text(
                  termText, textAlign = TextAlign.Center, modifier = Modifier.testTag("LoginTerms"))

              Spacer(Modifier.weight(0.2f))
            }
      }
}

val termText = buildAnnotatedString {
  append("By clicking continue, you agree to our \n")
  withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Terms of Service") }
  append(" and ")

  withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Privacy Policy") }
}

@Composable
fun SignInButton(onClick: () -> Unit) {

  Button(
      onClick = { onClick() },
      shape = RoundedCornerShape(20.dp),
      colors =
          ButtonDefaults.buttonColors(
              contentColor = MaterialTheme.colorScheme.onPrimary,
              containerColor = MaterialTheme.colorScheme.primaryContainer),
      modifier = Modifier.testTag("LoginButton")) {
        val imageModifierGoogle = Modifier.size(24.dp).absoluteOffset(x = (-13).dp, y = 0.dp)

        Image(
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = "Google_logo",
            contentScale = ContentScale.Fit,
            modifier = imageModifierGoogle)
        Text(text = "Sign in with Google")
      }
}
