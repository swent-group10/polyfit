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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.compose.Titre
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.theme.BlueButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun LoginScreen(navController: Navigation) {

    // Create an instance of the Authentication class
    val authentication = Authentication(navController, context = LocalContext.current)
    val signInLauncher =
        rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) { res ->
            authentication.onSignInResult(res)
        }

    // Set the signInLauncher in the Authentication class
    authentication.setSignInLauncher(signInLauncher)

    // This function starts the sign-in process
    fun createSignInIntent() {
        authentication.signIn()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("LoginScreen"),
        color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(0.2f))

            Titre(Modifier.testTag("LoginTitle"))

            Spacer(Modifier.weight(0.6f))

            ButtonSign {
                Log.d("LoginScreen", "button clicked")
                createSignInIntent()
            }

            Spacer(Modifier.weight(0.03f))

            Text(
                termText,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("TermsOfService")
            )

            Spacer(Modifier.weight(0.2f))
        }
    }
}

val termText = buildAnnotatedString {
    append("By clicking continue, you agree to our \n")
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append("Terms of Service")
    }
    append(" and ")

    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append("Privacy Policy")
    }
}
@Composable
fun ButtonSign(onClick : () -> Unit) {

    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = BlueButton
        ),
        modifier = Modifier.testTag("LoginButton")) {
        val imageModifierGoogle = Modifier
            .size(24.dp)
            .absoluteOffset(x = (-13).dp, y = 0.dp)

        Image(
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = "Google_logo",
            contentScale = ContentScale.Fit,
            modifier = imageModifierGoogle)
        Text(text = "Sign in with Google")
    }

}

class Authentication(private val navigation: Navigation, private val context: Context) {
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    fun setSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
        signInLauncher = launcher
    }

    fun signIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        Log.i("LoginScreen", "onSignInResult")
        val response = result.idpResponse
        Log.i("LoginScreen", "response: $response")
        if (result.resultCode == RESULT_OK) {
            Log.i("LoginScreen", "User signed in")
            navigation.navigateToHome()
        } else {
            response?.let {
                Log.e("LoginScreen", "Error in result firebase authentication: " + "${it.error?.errorCode}")
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    val nav = Navigation(NavHostController(LocalContext.current))
    LoginScreen(nav)
}