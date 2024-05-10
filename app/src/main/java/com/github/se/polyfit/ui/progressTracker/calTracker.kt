package com.github.se.polyfit.ui.progressTracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalTracker(
    progress: Float,
    text: String,
    trackColor: Color = Color.Gray,
    color: Color = Color.Black,
    strokeWidth: Float = 8f
) {
    require(progress >= 0f)
    var progressValue = progress

    //avoid any potential overflow
    if (progress > 1f) {
        progressValue = 1f
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            progress = {
                progress // Full circle
            },
            modifier = Modifier.size(200.dp),
            trackColor = trackColor,
            color = color,
            strokeWidth = strokeWidth.dp,
        )
        Text(
            text,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 18.sp,
                color = color,
                fontWeight = FontWeight.Bold

            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview
fun CalTrackerPreview() {
    CalTracker(.5f, "something")
}