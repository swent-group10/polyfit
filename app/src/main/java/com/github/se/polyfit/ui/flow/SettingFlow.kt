package com.github.se.polyfit.ui.flow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.polyfit.R
import com.github.se.polyfit.model.settings.SettingOption
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.SettingsScreen
import com.github.se.polyfit.ui.screen.settings.AccountSettingsScreen
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.viewmodel.settings.AccountSettingsViewModel
import com.github.se.polyfit.viewmodel.settings.SettingFlowViewModel

@Composable
fun SettingFlow(
    toLogin: () -> Unit,
    modifier: Modifier = Modifier,
    settingFlowViewModel: SettingFlowViewModel = hiltViewModel(),
    accountSettingsViewModel: AccountSettingsViewModel = hiltViewModel(),
) {
  val context = LocalContext.current
  val navController = rememberNavController()
  val navigation = Navigation(navController)
  var signoutDialog by remember { mutableStateOf(false) }

  fun signOut() {
    settingFlowViewModel.signOut()
    toLogin()
  }

  fun openSignoutDialog() {
    signoutDialog = true
  }

  val options =
      listOf(
          SettingOption("Account", Icons.Default.Person, navigation::navigateToAccountSettings),
          SettingOption(context.getString(R.string.signout), Icons.AutoMirrored.Default.ExitToApp, ::openSignoutDialog))

  NavHost(
      navController = navController, startDestination = Route.SettingsHome, modifier = modifier) {
        composable(Route.SettingsHome) {
          SettingsScreen(settings = options)
          if (signoutDialog) { SignoutDialog({ signoutDialog = false }, ::signOut) }
        }

        composable(Route.AccountSettings) {
          AccountSettingsScreen(navigation::navigateToSettingsHome, accountSettingsViewModel)
        }
      }
}

@Composable
private fun SignoutDialog(closeDialog: () -> Unit, signOut: () -> Unit) {
  val context = LocalContext.current

  Dialog(onDismissRequest = closeDialog) {
    GradientBox {
      Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(context.getString(R.string.confirmSignout))
        PrimaryButton(text = context.getString(R.string.signout), onClick = signOut, modifier = Modifier.testTag("SignoutButton"))
        PrimaryButton(
          text = context.getString(R.string.denyRequest),
          color = PrimaryPink,
          onClick = closeDialog,
          modifier = Modifier.testTag("DenyButton"))
      }
    }
  }
}
