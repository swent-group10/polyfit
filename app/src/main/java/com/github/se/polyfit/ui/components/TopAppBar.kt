package com.github.se.polyfit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.theme.PolyfitTheme
import com.github.se.polyfit.ui.utils.FunctionalityNotAvailablePopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithSearchDrawerAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = {},
    title: @Composable () -> Unit,
) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }
    AppBarWithPopup(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = title,
        actions = {
            SearchIcon(onClick = { functionalityNotAvailablePopupShown = true })
            UserIcon(onClick = { functionalityNotAvailablePopupShown = true })
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDrawerAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = {},
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }
    AppBarWithPopup(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = title,
        actions = { UserIcon(onClick = { functionalityNotAvailablePopupShown = true }) },

    )
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
            AppIcon(onClick = onNavIconPressed, modifier = Modifier.size(64.dp).padding(16.dp))
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WithSearchAppBarPreview() {
  PolyfitTheme(dynamicColor = false) { WithSearchDrawerAppBar(title = { Text("Preview!") }) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WithSearchAppBarPreviewDark() {
  PolyfitTheme(darkTheme = true, dynamicColor = false) {
    WithSearchDrawerAppBar(title = { Text("Preview!") })
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultAppBarPreview() {
  PolyfitTheme(dynamicColor = false) { DefaultDrawerAppBar(title = { Text("Preview!") }) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultAppBarPreviewDark() {
  PolyfitTheme(darkTheme = true, dynamicColor = false) {
    DefaultDrawerAppBar(title = { Text("Preview!") })
  }
}
