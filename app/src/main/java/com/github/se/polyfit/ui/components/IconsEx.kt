package com.github.se.polyfit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.github.se.polyfit.model.data.User

@Composable
fun UserIcon(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).size(38.dp)
) {

  val user = User.currentUser
  if (user?.photoURL != null) {
    // Load the user's profile image as a Painter and display it using Image
    val painter: Painter = rememberAsyncImagePainter(model = user.photoURL.toString())
    Image(
        painter = painter,
        contentDescription = "Google User Icon",
        modifier = modifier.clip(CircleShape).clickable(onClick = onClick))
  } else {
    // Use a default vector icon if the user does not have a profile image
    val imageVector: ImageVector = Icons.Outlined.AccountCircle
    Icon(
        imageVector = imageVector,
        contentDescription = "Default User Icon",
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.clickable(onClick = onClick))
  }
}

@Composable
fun AppIcon(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).size(38.dp)
) {
  // to replace with app logo or whatever
  Icon(
      imageVector = Icons.Outlined.Face,
      modifier = modifier.clickable(onClick = onClick),
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      contentDescription = "App Icon")
}

@Composable
fun SearchIcon(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).size(32.dp)
) {
  Icon(
      imageVector = Icons.Outlined.Search,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = modifier.clickable(onClick = onClick),
      contentDescription = "Search")
}

/*@Preview
@Composable
fun PreviewAppIcon() {
  PolyfitTheme(dynamicColor = false) { AppIcon() }
}

@Preview
@Composable
fun DarkPreviewAppIcon() {
  PolyfitTheme(dynamicColor = false, darkTheme = true) { AppIcon() }
}

@Preview
@Composable
fun PreviewSearchIcon() {
  PolyfitTheme(dynamicColor = false) { SearchIcon() }
}

@Preview
@Composable
fun DarkPreviewSearchIcon() {
  PolyfitTheme(dynamicColor = false, darkTheme = true) { SearchIcon() }
}

@Preview
@Composable
fun PreviewUserIcon() {
  PolyfitTheme(dynamicColor = false) { UserIcon() }
}

@Preview
@Composable
fun DarkPreviewUserIcon() {
  PolyfitTheme(dynamicColor = false, darkTheme = true) { UserIcon() }
}*/
