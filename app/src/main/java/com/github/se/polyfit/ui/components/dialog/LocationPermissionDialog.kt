package com.github.se.polyfit.ui.components.dialog

import android.Manifest
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.theme.PrimaryPurple

@Composable
fun LocationPermissionDialog(onDeny: () -> Unit, onApprove: () -> Unit) {

  val permissionLauncher = launcherForActivityResult(onDeny, onApprove)

  DisposableEffect(Unit) { onDispose {} }

  val context: Context = LocalContext.current
  AlertDialog(
      onDismissRequest = onDeny,
      title = {
        Text(
            context.getString(R.string.locationPermissionRequestTitle),
            modifier = Modifier.testTag("Title"),
        )
      },
      modifier = Modifier.testTag("LocationPermissionDialog"),
      text = {
        Text(
            text = context.getString(R.string.locationPermissionRequestMessage),
            fontSize = 16.sp,
            modifier = Modifier.testTag("Message"))
      },
      confirmButton = {
        Button(
            onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
            border = BorderStroke(3.dp, PrimaryPurple),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.testTag("ApproveButton"),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple, contentColor = Color.White)) {
              Text(context.getString(R.string.acceptRequest), fontSize = 16.sp)
            }
      },
      dismissButton = {
        Button(
            onClick = { onDeny() },
            border = BorderStroke(3.dp, PrimaryPurple),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.testTag("DenyButton"),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.White, contentColor = PrimaryPurple)) {
              Text(text = context.getString(R.string.denyRequest), fontSize = 16.sp)
            }
      },
      properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false))
}

@Composable
fun launcherForActivityResult(
    onDeny: () -> Unit,
    onApprove: () -> Unit,
    activityResult: ManagedActivityResultLauncher<String, Boolean> =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
          if (it) onApprove() else onDeny()
        }
): ManagedActivityResultLauncher<String, Boolean> {
  return activityResult
}
