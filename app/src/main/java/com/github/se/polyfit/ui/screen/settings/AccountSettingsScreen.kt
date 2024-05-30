package com.github.se.polyfit.ui.screen.settings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.common.extensions.isNotNull
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.ui.components.selector.DateSelector
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.PurpleGrey40
import com.github.se.polyfit.viewmodel.settings.AccountSettingsViewModel

@Composable
fun AccountSettingsScreen(
    goBack: () -> Unit,
    accountSettingsViewModel: AccountSettingsViewModel = hiltViewModel()
) {
  val context = LocalContext.current
  val user = accountSettingsViewModel.user.collectAsState().value
  val canSave =
      !user.givenName.isNullOrEmpty() &&
          !user.familyName.isNullOrEmpty() &&
          !user.displayName.isNullOrEmpty()

  Scaffold(
      topBar = { SimpleTopBar(getString(context, R.string.accountSettings), goBack) },
      bottomBar = {
        BottomBar(
            onSave = {
              accountSettingsViewModel.updateUser()
              goBack()
            },
            canSave = canSave,
            onCancel = goBack)
      }) {
        AccountSettings(accountSettingsViewModel, modifier = Modifier.padding(it))
      }
}

@Composable
private fun AccountSettings(
    accountSettingsViewModel: AccountSettingsViewModel,
    modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val user = accountSettingsViewModel.user.collectAsState().value

  var height by remember { mutableStateOf(user.heightCm?.toString() ?: "") }
  var weight by remember { mutableStateOf(user.weightKg?.toString() ?: "") }
  var calorieGoal by remember {
    mutableStateOf(if (user.calorieGoal > 0) user.calorieGoal.toString() else "")
  }

  LazyColumn(modifier = modifier.padding(horizontal = 16.dp).testTag("AccountSettingsScreen")) {
    item {
      TextField(
          value = user.displayName ?: "",
          placeholder = { Text(getString(context, R.string.accountSettingsDisplayName)) },
          label = { Text(getString(context, R.string.accountSettingsDisplayName)) },
          onValueChange = { newUsername ->
            if (newUsername.length <= 20) {
              accountSettingsViewModel.updateDisplayName(newUsername)
            }
          },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("DisplayName"))
    }
    item {
      TextField(
          value = user.givenName ?: "",
          placeholder = { Text(getString(context, R.string.accountSettingsFirstName)) },
          label = { Text(getString(context, R.string.accountSettingsFirstName)) },
          onValueChange = accountSettingsViewModel::updateFirstName,
          singleLine = true,
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("FirstName"))
    }
    item {
      TextField(
          value = user.familyName ?: "",
          placeholder = { Text(getString(context, R.string.accountSettingsLastName)) },
          label = { Text(getString(context, R.string.accountSettingsLastName)) },
          onValueChange = accountSettingsViewModel::updateLastName,
          singleLine = true,
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("LastName"))
    }
    item {
      TextField(
          value = height,
          placeholder = { Text(getString(context, R.string.accountSettingsHeight)) },
          label = { Text(getString(context, R.string.accountSettingsHeight)) },
          keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
          singleLine = true,
          onValueChange = {
            var update = it.toLongOrNull()

            // If there is a Long overflow, the use the previous value
            if (update == null && it.toBigDecimalOrNull() != null) {
              update = height.toLongOrNull()
            }

            height =
                when {
                  update == null || update <= 0 -> ""
                  else -> update.toString()
                }
            accountSettingsViewModel.updateHeight(
                when {
                  update.isNotNull() && update!! > 0 -> update
                  else -> null
                })
          },
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("Height"))
    }
    item {
      TextField(
          value = weight,
          placeholder = { Text(getString(context, R.string.accountSettingsWeight)) },
          label = { Text(getString(context, R.string.accountSettingsWeight)) },
          keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
          singleLine = true,
          onValueChange = {
            var update = it.toLongOrNull()

            // If there is a Double overflow, the use the previous value
            Log.d("AccountSettings", "update: $update")
            Log.d("AccountSettings", "updateBig: ${it.toBigDecimalOrNull()}")

            if (update == null && it.toBigDecimalOrNull() != null) {
              update = weight.toLongOrNull()
            }

            weight =
                when {
                  update == null || update!! <= 0 -> ""
                  else -> update.toString()
                }
            accountSettingsViewModel.updateWeight(
                when {
                  update.isNotNull() && update!! > 0 -> update!!.toDouble()
                  else -> null
                })
          },
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("Weight"))
    }
    item {
      TextField(
          value = calorieGoal,
          placeholder = { Text(getString(context, R.string.accountSettingsCalorieGoal)) },
          label = { Text(getString(context, R.string.accountSettingsCalorieGoal)) },
          keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
          singleLine = true,
          onValueChange = {
            var update = it.toLongOrNull()

            if (update == null && it.toBigDecimalOrNull() != null) {
              update = calorieGoal.toLongOrNull()
            }

            calorieGoal =
                when {
                  update.isNotNull() -> update.toString()
                  else -> ""
                }
            accountSettingsViewModel.updateCalorieGoal(update ?: 0)
          },
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("CalorieGoal"))
    }

    item {
      DateSelector(
          onConfirm = accountSettingsViewModel::updateDob,
          title = getString(context, R.string.accountSettingsDob),
          titleStyle = MaterialTheme.typography.titleMedium,
          inputDate = user.dob,
          modifier = Modifier.padding(vertical = 8.dp),
          allowFutureDates = false)
    }

    // TODO: Add interface to set dietary preferences
  }
}

@Composable
private fun BottomBar(onSave: () -> Unit, canSave: Boolean, onCancel: () -> Unit) {
  val context = LocalContext.current

  BottomAppBar(containerColor = Color.Transparent) {
    Row(
        modifier = Modifier.fillMaxWidth().testTag("BottomBarRow"),
        horizontalArrangement = Arrangement.End) {
          TextButton(
              modifier = Modifier.testTag("Cancel"),
              colors = ButtonDefaults.textButtonColors(contentColor = PurpleGrey40),
              onClick = onCancel) {
                Text(getString(context, R.string.accountSettingsCancel))
              }
          TextButton(
              modifier = Modifier.testTag("Save"),
              colors = ButtonDefaults.textButtonColors(contentColor = PrimaryPurple),
              enabled = canSave,
              onClick = onSave) {
                Text(getString(context, R.string.accountSettingsSave))
              }
        }
  }
}

@Preview
@Composable
fun AccountSettingsScreenPreview() {
  AccountSettingsScreen(goBack = {})
}
