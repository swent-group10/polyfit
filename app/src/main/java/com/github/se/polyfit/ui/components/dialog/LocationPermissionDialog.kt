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
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Priority

@Composable
fun LocationPermissionDialog(onDeny: () -> Unit, onApprove: (CurrentLocationRequest) -> Unit) {

  val locationPermissionRequest = launcherForActivityResult(onDeny, onApprove)

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
            onClick = {
              locationPermissionRequest.launch(
                  arrayOf(
                      Manifest.permission.ACCESS_FINE_LOCATION,
                      Manifest.permission.ACCESS_COARSE_LOCATION))
            },
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
    onApproveForPost: (CurrentLocationRequest) -> Unit,
    onApproveForMap: () -> Unit = {},
    requestForMap: Boolean = false
): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {

  return rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
      permissions ->
    when {
      permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
        if (requestForMap) {
          onApproveForMap()
        } else {
          onApproveForPost(
              CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build())
        }
      }
      permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
        if (requestForMap) {
          onApproveForMap()
        } else {
          onApproveForPost(
              CurrentLocationRequest.Builder()
                  .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                  .build())
        }
      }
      else -> {
        onDeny()
      }
    }
  }
}
