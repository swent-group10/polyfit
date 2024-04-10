package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.utils.FunctionalityNotAvailablePopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithSearchDrawerAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = {},
    title: String
) {
  var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
  if (functionalityNotAvailablePopupShown) {
    FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
  }
  AppBarWithPopup(
      modifier = modifier,
      scrollBehavior = scrollBehavior,
      title = { Title(title) },
      onNavIconPressed = onNavIconPressed,
      actions = {
        SearchIcon(onClick = { functionalityNotAvailablePopupShown = true })
        UserIcon(onClick = { functionalityNotAvailablePopupShown = true })
      })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDrawerAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = {},
    title: String
) {
  var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
  if (functionalityNotAvailablePopupShown) {
    FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
  }
  AppBarWithPopup(
      modifier = modifier,
      scrollBehavior = scrollBehavior,
      title = { Title(title, withSearch = false) },
      onNavIconPressed = onNavIconPressed,
      actions = { UserIcon(onClick = { functionalityNotAvailablePopupShown = true }) },
  )
}

@Composable
private fun Title(title: String, withSearch: Boolean = true) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .then(if (withSearch) Modifier.padding(start = 45.dp) else Modifier),
      horizontalArrangement = if (withSearch) Arrangement.Start else Arrangement.Center) {
        Text(text = title)
      }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBarWithPopup(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    onNavIconPressed: () -> Unit = {}
) {

  CenterAlignedTopAppBar(
      modifier = modifier,
      actions = actions,
      title = title,
      scrollBehavior = scrollBehavior,
      navigationIcon = {
        AppIcon(
            onClick = onNavIconPressed,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).size(38.dp))
      })
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WithSearchAppBarPreview() {
  PolyfitTheme(dynamicColor = false) { WithSearchDrawerAppBar(title = "Preview!") }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WithSearchAppBarPreviewDark() {
  PolyfitTheme(darkTheme = true, dynamicColor = false) {
    WithSearchDrawerAppBar(title = "Preview!")
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultAppBarPreview() {
  PolyfitTheme(dynamicColor = false) { DefaultDrawerAppBar(title = "Preview!") }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultAppBarPreviewDark() {
  PolyfitTheme(darkTheme = true, dynamicColor = false) { DefaultDrawerAppBar(title = "Preview") }
}*/
