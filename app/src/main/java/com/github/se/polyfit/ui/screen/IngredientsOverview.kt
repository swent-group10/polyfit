package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.IngredientsOverview.BottomBarIngredient
import com.github.se.polyfit.ui.components.IngredientsOverview.ListProducts
import com.github.se.polyfit.ui.components.button.FloatingActionButtonIngredients
import com.github.se.polyfit.ui.components.scaffold.SimpleTopBar
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel

// TODO THIS CLASS IS TEMPORARY, REMOVE IT WHEN DOING THE VIEWMODEL
data class IngredientsTMP(
    val name: String,
    val servingSize: Int,
    val calories: Int,
    val carbs: Int,
    val fat: Int,
    val protein: Int,
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
        listProducts: List<IngredientsTMP>,
        barCodeCodeViewModel: BarCodeCodeViewModel = hiltViewModel()
) {
  val context = LocalContext.current

  val listId by barCodeCodeViewModel.listId.observeAsState()

  val listProducts = mutableListOf<IngredientsTMP>()
  for (qrCode in listId ?: emptyList()) {
    listProducts += IngredientsTMP(qrCode, 0, 0, 0, 0, 0)
  }


  Scaffold(
      topBar = { SimpleTopBar(title = context.getString(R.string.Product)) { navigateBack() } },
      bottomBar = { BottomBarIngredient(navigateForward = navigateForward) },
      floatingActionButton = { FloatingActionButtonIngredients(onClickFloatingButton) },
      containerColor = MaterialTheme.colorScheme.background,
      modifier = Modifier.testTag("IngredientsOverviewScaffold")) {
        CameraPreviewScreen()

        // TODO THIS IS TEMPORARY, REMOVE IT WHEN DOING THE VIEWMODEL
        ListProducts(listIngredients = listProducts, Modifier
                .fillMaxSize()
                .padding(0.dp, it.calculateTopPadding() + 200.dp, 0.dp, it.calculateBottomPadding()))
      }
}
