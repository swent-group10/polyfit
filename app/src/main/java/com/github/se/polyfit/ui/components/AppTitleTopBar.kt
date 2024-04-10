package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.github.se.polyfit.ui.compose.Title


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTitle(
    titleText: String = "Polyfit",
    alignment: Alignment = Alignment.TopCenter
) {
    TopAppBar(
        modifier = Modifier.testTag("MainTopBar"),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("MainTopBar"),
                contentAlignment = alignment
            ) {
                Title(modifier = Modifier.testTag("TopBarTitle"), titleText)
            }
        }
    )
}