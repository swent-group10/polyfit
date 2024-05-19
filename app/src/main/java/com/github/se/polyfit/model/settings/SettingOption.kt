package com.github.se.polyfit.model.settings

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingOption(
    val title: String,
    val icon: ImageVector,
    val navigateTo: () -> Unit,
)
