package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.R
import com.github.se.polyfit.model.settings.SettingOption

@Composable
fun SettingsScreen(settings: List<SettingOption>, modifier: Modifier = Modifier) {
  val context = LocalContext.current

  Column(
      modifier = modifier.fillMaxSize().testTag("SettingsScreen"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top) {
        Text(
            text = context.getString(R.string.settings),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(48.dp).testTag("SettingsTitle"))

        LazyColumn(modifier = Modifier.testTag("SettingsList")) {
          items(settings.size) { index ->
            SettingItem(
                settingOption = settings[index],
            )
          }
        }
      }
}

@Composable
private fun SettingItem(settingOption: SettingOption) {
  val navigateTo = settingOption.navigateTo
  val title = settingOption.title
  val icon = settingOption.icon

  Column(modifier = Modifier.padding(horizontal = 32.dp).testTag("SettingItemColumn")) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
            Modifier.fillMaxWidth().padding(16.dp).testTag("SettingItem").clickable {
              navigateTo()
            }) {
          Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.testTag("TitleRow")) {
                Icon(
                    imageVector = icon,
                    contentDescription = icon.name,
                    modifier = Modifier.testTag("Icon"))
                Text(text = title, modifier = Modifier.padding(start = 8.dp).testTag("Title"))
              }
          Icon(
              imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = "ArrowRight",
              modifier = Modifier.testTag("ArrowRight"))
        }
    HorizontalDivider(modifier = Modifier.testTag("Divider"))
  }
}

// @Preview
// @Composable
// fun PreviewSettingsScreen() {
//  val settings: List<SettingOption> = listOf(
//    SettingOption("Account", Icons.Outlined.AccountCircle, {}),
//    SettingOption("Terms & Conditions", Icons.Outlined.Build, {}),
//    SettingOption("Privacy Policy", Icons.Outlined.Lock, {}),
//    SettingOption("About", Icons.Outlined.Info, {}),
//    SettingOption("Logout", Icons.AutoMirrored.Outlined.ExitToApp, {}),
//  )
//
//  SettingsScreen(settings)
// }
