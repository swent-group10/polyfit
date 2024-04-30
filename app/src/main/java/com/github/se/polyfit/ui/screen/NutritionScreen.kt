package com.github.se.polyfit.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.nutrition.NutritionalInformation
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NutritionScreen(
    mealViewModel: MealViewModel,
    navigateBack: () -> Unit,
    navigateForward: () -> Unit
) {
    val isComplete by mealViewModel.isComplete.collectAsState(initial = false)
    Log.d(
        "NutritionScreen",
        "isComplete: $isComplete"
    )
    Log.d(
        "NutritionScreen",
        "mealViewModel: ${mealViewModel.meal.value}"

    )

    Scaffold(
        topBar = { TopBar(navigateBack = navigateBack) },
        bottomBar = {
            BottomBar(
                setMeal = mealViewModel::setMeal,
                isComplete = isComplete,
                navigateForward = navigateForward
            )
        }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) { NutritionalInformation(mealViewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                "Nutrition Facts",
                modifier = Modifier.testTag("Title"),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack() },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.testTag("BackButton"),
                        tint = PrimaryPurple
                    )
                },
                modifier = Modifier.testTag("BackButton")
            )
        },
        modifier = Modifier.testTag("TopBar")
    )
}

@Composable
private fun BottomBar(setMeal: () -> Unit, isComplete: Boolean, navigateForward: () -> Unit) {
    BottomAppBar(
        modifier = Modifier
            .height(128.dp)
            .testTag("BottomBar"), containerColor = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ButtonColumn"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                onClick = { Log.v("Add Recipe", "Clicked") },
                modifier = Modifier
                    .width(250.dp)
                    .testTag("AddRecipeButton"),
                text = "Add Recipe",
                fontSize = 18,
                color = PrimaryPink
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                onClick = {
                    Log.v("Add to Diary", "Clicked")
                    setMeal()
                    navigateForward()
                },
                modifier = Modifier
                    .width(250.dp)
                    .testTag("AddToDiaryButton"),
                text = "Add to Diary",
                fontSize = 18,
                isEnabled = isComplete,
                color = PrimaryPurple,
            )
        }
    }
}
