package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreatePostScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreatePostScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CreatePostScreen") }) {

  val pictureSelector: KNode = child { hasTestTag("PictureSelector") }
  val postDescription: KNode = child { hasTestTag("PostDescription") }
  val mealSelector: KNode = child { hasTestTag("MealSelector") }
}

class CreatePostAddMeal(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreatePostAddMeal>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("CreateMeal") }) {

  val noMealsFound: KNode = child { hasTestTag("NoMealsFound") }
  val addMealButton: KNode = child { hasTestTag("AddMealButton") }
}

class CreatePostTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreatePostTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("TopBar") }) {

  val title: KNode = child { hasTestTag("New Post Title") }
  val backButton: KNode = child { hasTestTag("BackButton") }
}

class CreatePostBottomBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreatePostBottomBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BottomBar") }) {

  private val postBox: KNode = child { hasTestTag("PostBox") }
  val postButton: KNode = postBox.child { hasTestTag("PostButton") }
}
