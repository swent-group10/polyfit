package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.components.IngredientsOverview.ListProducts
import com.github.se.polyfit.ui.theme.PolyfitTheme
import com.github.se.polyfit.ui.components.IngredientsOverview.TopBar


// TODO THIS CLASS IS TEMPORARY, REMOVE IT WHEN DOING THE VIEWMODEL
data class IngredientsTMP(
    val name: String,
    val servingSize: Int,
    val calories: Int,
    val carbs: Int,
    val fat: Int,
    val protein: Int
)

val i1 = IngredientsTMP("Apple", 100, 52, 14, 0, 0)
val i2 = IngredientsTMP("Banana", 100, 89, 23, 0, 1)
val i3 = IngredientsTMP("Carrot", 100, 41, 10, 0, 1)
val i4 = IngredientsTMP("Date", 100, 282, 75, 0, 2)
val i5 = IngredientsTMP("Eggplant", 100, 25, 6, 0, 1)

private val ListProducts = listOf(i1, i2, i3, i4, i5)

@Preview
@Composable
private fun PreviewIngredientsOverview() {
  PolyfitTheme(darkTheme = false, dynamicColor = false) { IngredientsOverview() }
}

@Composable
fun IngredientsOverview(
    navigateBack: () -> Unit = {},
    navigateForward: () -> Unit = {},
    onClickFloatingButton: () -> Unit = {}
) {

  Scaffold(
      topBar = { TopBar { navigateBack() } },
      bottomBar = { com.github.se.polyfit.ui.components.IngredientsOverview.BottomBar(navigateForward = navigateForward) },
      floatingActionButton = { com.github.se.polyfit.ui.components.button.FloatingActionButton(onClickFloatingButton) },
      containerColor = MaterialTheme.colorScheme.background,
  ) {
    ListProducts(ListProducts = ListProducts, Modifier.fillMaxSize().padding(it))
  }
}




