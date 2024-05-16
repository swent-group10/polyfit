package com.github.se.polyfit.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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

@Composable
fun PictureDialog(
    onDismiss: () -> Unit,
    onButtonsClick: List<() -> Unit>,
    buttonsName: List<String>,
) {
  assert(buttonsName.size == onButtonsClick.size)
  val context = LocalContext.current
  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {},
      modifier = Modifier.height(330.dp).testTag("PictureDialog"),
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
              val buttons = buttonsName.zip(onButtonsClick)

              buttons.forEachIndexed { index, (buttonName, buttonClick) ->
                Button(
                    onClick = buttonClick,
                    modifier = Modifier.width(180.dp).padding(10.dp).testTag("${index}Button")) {
                      Text(text = buttonName, modifier = Modifier.testTag("${index}ButtonName"))
                    }
              }
            }
      })
}
