package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.R
import com.github.se.polyfit.data.local.ingredientscanned.IngredientsScanned
import com.github.se.polyfit.ui.components.IngredientsOverview.BottomBarIngredient
import com.github.se.polyfit.ui.components.IngredientsOverview.ListProducts
import com.github.se.polyfit.ui.components.button.FloatingActionButtonIngredients
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel

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
    listProducts: List<IngredientsScanned>,
    barCodeCodeViewModel: BarCodeCodeViewModel = hiltViewModel()
) {
  val context = LocalContext.current

  var listIngredients by remember { mutableStateOf(listProducts) }
  LaunchedEffect(true) {
    barCodeCodeViewModel.listIngredients.observeForever { listIngredients = it }
  }

  Scaffold(
      topBar = { SimpleTopBar(title = context.getString(R.string.Product)) { navigateBack() } },
      bottomBar = {
        BottomBarIngredient(
            navigateForward = {
              barCodeCodeViewModel.setIngredients()
              navigateForward()
            })
      },
      floatingActionButton = { FloatingActionButtonIngredients(onClickFloatingButton) },
      containerColor = MaterialTheme.colorScheme.background,
      modifier = Modifier.testTag("IngredientsOverviewScaffold")) {
        CameraXScreen(barCodeCodeViewModel)

        ListProducts(
            listIngredients = listIngredients,
            Modifier.fillMaxSize()
                .padding(
                    0.dp, it.calculateTopPadding() + 200.dp, 0.dp, it.calculateBottomPadding()))
      }
}
