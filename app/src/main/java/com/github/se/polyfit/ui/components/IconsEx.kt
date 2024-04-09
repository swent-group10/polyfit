package com.github.se.polyfit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuOpen
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.theme.PolyfitTheme

@Composable
fun UserIcon(onClick: () -> Unit) {
  val modifier =
      Modifier.clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp).size(32.dp)

  val user = User.currentUser
  if (user?.photoURL != null) {
    // Load the user's profile image as a Painter and display it using Image
    val painter: Painter = rememberAsyncImagePainter(model = user.photoURL)
    Icon(painter = painter, contentDescription = "Google User Icon", modifier = modifier)
  } else {
    // Use a default vector icon if the user does not have a profile image
    val imageVector: ImageVector = Icons.Outlined.AccountCircle
    Icon(
        imageVector = imageVector,
        contentDescription = "Default User Icon",
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier)
  }
}

@Composable
fun AppIcon(onClick: () -> Unit, modifier: Modifier = Modifier) {
  // to replace with app logo or whatever
  Icon(
      imageVector = Icons.AutoMirrored.Outlined.MenuOpen,
      modifier = modifier.clickable(onClick = onClick),
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      contentDescription = "App Icon")
}

@Composable
fun SearchIcon(onClick: () -> Unit) {
  Icon(
      imageVector = Icons.Outlined.Search,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier =
          Modifier.clickable(onClick = onClick)
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .size(32.dp),
      contentDescription = "Search")
}

@Preview
@Composable
fun PreviewAppIcon() {
  PolyfitTheme(dynamicColor = false) { AppIcon(onClick = { /*TODO: Handle onClick*/}) }
}

@Preview
@Composable
fun DarkPreviewAppIcon() {
  PolyfitTheme(dynamicColor = false, darkTheme = true) {
    AppIcon(onClick = { /*TODO: Handle onClick*/})
  }
}

@Preview
@Composable
fun PreviewSearchIcon() {
  PolyfitTheme(dynamicColor = false){SearchIcon(onClick = { /*TODO: Handle onClick*/})}
}

@Preview
@Composable
fun DarkPreviewSearchIcon() {
    PolyfitTheme(dynamicColor = false, darkTheme = true){SearchIcon(onClick = { /*TODO: Handle onClick*/})}
}

@Preview
@Composable
fun PreviewUserIcon() {
  PolyfitTheme(dynamicColor = false){ UserIcon(onClick = { /*TODO: Handle onClick*/})}
}

@Preview
@Composable
fun DarkPreviewUserIcon() {
    PolyfitTheme(dynamicColor = false, darkTheme = true){ UserIcon(onClick = { /*TODO: Handle onClick*/})}
}
