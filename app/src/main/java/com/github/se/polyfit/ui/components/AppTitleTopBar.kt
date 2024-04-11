package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.github.se.polyfit.ui.utils.OverviewTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTitle(titleText: String = "Polyfit", alignment: Alignment = Alignment.TopCenter) {
  TopAppBar(
      modifier = Modifier.testTag("MainTopBar"),
      title = {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
          GradientBox(outerModifier = Modifier.fillMaxWidth(0.8f).testTag("topBarOuterBox")) {
            // align box at the center
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
              Text(
                  text = titleText,
                  style = MaterialTheme.typography.titleLarge,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.align(alignment).testTag(OverviewTags.overviewTitle),
                  fontWeight = FontWeight.ExtraBold)
            }
          }
        }
      })
}
