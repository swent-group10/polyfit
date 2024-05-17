package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.github.se.polyfit.ui.components.IngredientsOverview.BottomBarIngredient
import com.github.se.polyfit.ui.components.IngredientsOverview.ListProducts
import com.github.se.polyfit.ui.components.IngredientsOverview.TopBarIngredient
import com.github.se.polyfit.ui.components.button.FloatingActionButtonIngredients

// TODO THIS CLASS IS TEMPORARY, REMOVE IT WHEN DOING THE VIEWMODEL
data class IngredientsTMP(
    val name: String,
    val servingSize: Int,
    val calories: Int,
    val carbs: Int,
    val fat: Int,
    val protein: Int
)

/*val i1 = IngredientsTMP("Apple", 100, 52, 14, 0, 0)

val listProducts = listOf(i1)

@Preview
@Composable
private fun PreviewIngredientsOverview() {
  PolyfitTheme {
    IngredientsOverview({}, {}, {}, listProducts = listProducts)
  }
}
 */

@Composable
fun IngredientsOverview(
    navigateBack: () -> Unit,
    navigateForward: () -> Unit,
    onClickFloatingButton: () -> Unit,
    listProducts: List<IngredientsTMP>
) {

  Scaffold(
      topBar = { TopBarIngredient { navigateBack() } },
      bottomBar = { BottomBarIngredient(navigateForward = navigateForward) },
      floatingActionButton = { FloatingActionButtonIngredients(onClickFloatingButton) },
      containerColor = MaterialTheme.colorScheme.background,
      modifier = Modifier.testTag("IngredientsOverviewScaffold")) {
        ListProducts(listProducts = listProducts, Modifier.fillMaxSize().padding(it))
      }
}
