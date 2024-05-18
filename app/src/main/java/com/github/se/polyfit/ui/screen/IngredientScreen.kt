package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.ui.components.button.GradientButton
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.dialog.AddIngredientDialog
import com.github.se.polyfit.ui.components.ingredients.IngredientList
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.viewmodel.meal.MealViewModel

@Composable
fun IngredientScreen(
    mealViewModel: MealViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateForward: () -> Unit
) {

    val showAddIngredDialog = remember { mutableStateOf(false) }
    val enabled = mealViewModel.meal.collectAsState().value.ingredients.isNotEmpty()

    fun goBackAndReset() {
        navigateBack()
    }

    Scaffold(
        topBar = { TopBar(::goBackAndReset) },
        bottomBar = {
            BottomBar(
                onClickAddIngred = { showAddIngredDialog.value = true },
                navigateForward = navigateForward,
                enabled = enabled
            )
        }) {
        IngredientList(it, mealViewModel)
        if (showAddIngredDialog.value) {
            AddIngredientDialog(
                onClickCloseDialog = { showAddIngredDialog.value = false },
                onAddIngredient = mealViewModel::addIngredient
            )
        }
    }
}

@Composable
private fun BottomBar(onClickAddIngred: () -> Unit, navigateForward: () -> Unit, enabled: Boolean) {
    Column(
        modifier =
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(0.dp, 16.dp, 0.dp, 32.dp)
            .testTag("BottomBar"),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
                .testTag("AddIngredientBox"),
            contentAlignment = Alignment.CenterEnd
        ) {
            GradientButton(
                onClick = {
                    Log.v("Add Ingredient", "Clicked")
                    onClickAddIngred()
                },
                active = true,
                round = true,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Ingredient",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.testTag("AddIngredientButton")
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("DoneBox"),
            contentAlignment = Alignment.Center
        ) {
            PrimaryButton(
                onClick = {
                    navigateForward()
                    Log.v("Finished", "Clicked")
                },
                text = "Done",
                isEnabled = enabled,
                fontSize = 24,
                modifier = Modifier
                    .width(200.dp)
                    .testTag("DoneButton")
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigateBack: () -> Unit) {
    var wasClicked by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                "Ingredients",
                modifier = Modifier.testTag("IngredientTitle"),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!wasClicked) {
                        navigateBack()
                        wasClicked = true
                    }
                },
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

// @Composable
// fun IngredientScreenPreview() {
//  IngredientScreen(Navigation(rememberNavController()), listOf(), listOf())
// }
