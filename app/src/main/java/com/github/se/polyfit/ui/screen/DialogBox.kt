package com.github.se.polyfit.ui.screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureDialog(onDismiss: () -> Unit, onTakePic: () -> Unit, onImportPic: () -> Unit) {
  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {},
      modifier = Modifier.height(250.dp),
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "Choose Option", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        }
      },
      text = {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
              Button(onClick = onTakePic, modifier = Modifier.width(180.dp).padding(10.dp)) {
                Text("Take Photo")
              }
              Button(onClick = onImportPic, modifier = Modifier.width(180.dp).padding(10.dp)) {
                Text("Import Photo")
              }
            }
      })
}
