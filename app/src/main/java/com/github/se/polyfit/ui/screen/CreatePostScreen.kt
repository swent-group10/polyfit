package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.dialog.LocationPermissionDialog
import com.github.se.polyfit.ui.components.scaffold.CenteredTopBar
import com.github.se.polyfit.ui.components.selector.MealSelector
import com.github.se.polyfit.ui.components.selector.PictureSelector
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.SecondaryGrey
import com.github.se.polyfit.viewmodel.post.CreatePostViewModel
import com.google.android.gms.location.CurrentLocationRequest

/**
 * CreatePostScreen is the screen where the user can create a new post. The user can select a meal,
 * add a picture, and write a description.
 *
 * @param navigateBack: Function to navigate back to the previous screen.
 * @param navigateForward: Function to navigate forward to the next screen.
 * @param navigateToAddMeal: Function to navigate to the AddMealScreen.
 * @param postViewModel: ViewModel for the CreatePostScreen.
 */
@Composable
fun CreatePostScreen(
    navigateBack: () -> Unit = {},
    navigateForward: () -> Unit = {},
    navigateToAddMeal: () -> Unit = {},
    postViewModel: CreatePostViewModel = hiltViewModel()
) {
  val context = LocalContext.current
  val meals by postViewModel.meals.collectAsState()
  var isPermissionDialogDisplay by remember { mutableStateOf(false) }
  var selectedMeal by remember { mutableStateOf(Meal.default()) }
  val postComplete by remember(selectedMeal) { derivedStateOf { selectedMeal.isComplete() } }

  fun setPostMeal(meal: Meal) {
    postViewModel.setPostData(meal = meal)
  }

  fun onApprove(locationRequest: CurrentLocationRequest) {
    isPermissionDialogDisplay = false
    postViewModel.setInOrder(locationRequest)
    navigateForward()
  }

  Scaffold(
      topBar = {
        CenteredTopBar(
            title = context.getString(R.string.newPostTitle), navigateBack = navigateBack)
      },
      bottomBar = {
        BottomBar(
            postComplete = postComplete, onButtonPressed = { isPermissionDialogDisplay = true })
      }) {
        if (meals.isEmpty()) {
          CreateMeal(navigateToAddMeal, Modifier.padding(it))
          return@Scaffold
        }

        LazyColumn(modifier = Modifier.padding(it).testTag("CreatePostScreen")) {
          item {
            PictureSelector(
                modifier = Modifier.padding(top = 10.dp),
                postViewModel::getBitMap,
                postViewModel::setBitMap)
          }
          item { PostDescription(postViewModel::setPostDescription) }
          item { HorizontalDivider(thickness = 1.dp, color = Color.LightGray) }
          item { MealSelector(selectedMeal, { meal -> selectedMeal = meal }, meals, ::setPostMeal) }
        }

        if (isPermissionDialogDisplay) {
          LocationPermissionDialog(
              onApprove = ::onApprove, onDeny = { isPermissionDialogDisplay = false })
        }
      }
}

/**
 * CreateMeal is a composable function that displays a message to the user if there are no meals
 * found in the database. The user can then navigate to the AddMealScreen to add a new meal.
 *
 * @param navigateToAddMeal: Function to navigate to the AddMealScreen.
 * @param modifier: Modifier to apply to the Column.
 */
@Composable
private fun CreateMeal(navigateToAddMeal: () -> Unit, modifier: Modifier = Modifier) {
  val context = LocalContext.current
  Column(
      modifier = modifier.fillMaxSize().testTag("CreateMeal"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = context.getString(R.string.noMealsFound),
            color = SecondaryGrey,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).testTag("NoMealsFound"))
        PrimaryButton(
            text = context.getString(R.string.addMealButton),
            onClick = navigateToAddMeal,
            color = PrimaryPink,
            fontSize = 24,
            modifier = Modifier.padding(bottom = 16.dp).testTag("AddMealButton"))
      }
}

/**
 * PostDescription is a composable function that displays a TextField for the user to write a
 * description for the post.
 *
 * @param setPostDescription: Function to set the description of the post.
 */
@Composable
private fun PostDescription(setPostDescription: (String) -> Unit) {
  val context = LocalContext.current
  var descriptionText by remember { mutableStateOf("") }
  val keyboardController = LocalSoftwareKeyboardController.current

  TextField(
      value = descriptionText,
      onValueChange = {
        descriptionText = it
        setPostDescription(it)
      },
      modifier = Modifier.fillMaxWidth().heightIn(max = 150.dp).testTag("PostDescription"),
      textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
      leadingIcon = {
        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Pen", tint = SecondaryGrey)
      },
      placeholder = {
        Text(
            text = context.getString(R.string.newPostDescriptionPlaceholder),
            color = SecondaryGrey,
            fontSize = 20.sp)
      },
      colors =
          TextFieldDefaults.colors(
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent,
              focusedIndicatorColor = Color.Transparent,
              unfocusedIndicatorColor = Color.Transparent,
              cursorColor = PrimaryPurple,
          ),
      // Set the keyboard options to Done so that the keyboard can be hidden when the user is done
      keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
      keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }))
}

/**
 * BottomBar is a composable function that displays a button at the bottom of the screen.
 *
 * @param onButtonPressed: Function to execute when the button is pressed.
 * @param postComplete: Boolean to determine if the post is complete.
 */
@Composable
private fun BottomBar(onButtonPressed: () -> Unit, postComplete: Boolean) {
  val context = LocalContext.current
  Column(
      modifier =
          Modifier.background(MaterialTheme.colorScheme.background)
              .padding(0.dp, 16.dp, 0.dp, 32.dp)
              .testTag("BottomBar"),
  ) {
    Box(
        modifier = Modifier.fillMaxWidth().testTag("PostBox"),
        contentAlignment = Alignment.Center) {
          PrimaryButton(
              onClick = {
                onButtonPressed()
                Log.v("Finished", "Clicked")
              },
              text = context.getString(R.string.postButton),
              fontSize = 24,
              modifier = Modifier.width(200.dp).testTag("PostButton"),
              color = PrimaryPink,
              isEnabled = postComplete,
              buttonShape = RoundedCornerShape(12.dp))
        }
  }
}
