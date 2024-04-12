package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureDialog(
    onDismiss: () -> Unit,
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    firstButtonName: String,
    secondButtonName: String
) {
  val context = LocalContext.current
  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {},
      modifier = Modifier.height(250.dp).testTag("PictureDialog"),
      title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("PictureDialogRow")) {
              Text(
                  text = context.getString(R.string.dialog_headline),
                  fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.testTag("PictureDialogTitle"))
            }
      },
      text = {
        Column(
            modifier = Modifier.fillMaxSize().testTag("PictureDialogColumn"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
              Button(
                  onClick = onFirstButtonClick,
                  modifier = Modifier.width(180.dp).padding(10.dp).testTag("FirstButton")) {
                    Text(text = firstButtonName, modifier = Modifier.testTag("FirstButtonName"))
                  }
              Button(
                  onClick = onSecondButtonClick,
                  modifier = Modifier.width(180.dp).padding(10.dp).testTag("SecondButton")) {
                    Text(text = secondButtonName, modifier = Modifier.testTag("SecondButtonName"))
                  }
            }
      })
}
