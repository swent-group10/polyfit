package com.github.se.polyfit.viewmodel.qrCode

import android.content.Context
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.data.local.ingredientscanned.IngredientsScanned
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// We need to scan multiple times the same value to be sure it's not a mistake
const val REQUIRED_SCAN_COUNT = 3

// A barcode is between 6 and 13 characters
const val MIN_BARCODE_LENGTH = 6
const val MAX_BARCODE_LENGTH = 13
const val TIME_COLOR_BORDER_MILLIS = 2000L

/**
 * ViewModel for the barcode scanner. It is responsible for handling the barcode scanning and
 * storing the scanned values.
 */
@HiltViewModel
class BarCodeCodeViewModel
@Inject
constructor(
    private val recipeRecommendationViewModel: RecipeRecommendationViewModel,
    private val foodFactsApi: OpenFoodFactsApi
) : ViewModel() {
  private val _listId = MutableLiveData<List<String>>(emptyList())
  private val _isScanned = MutableLiveData(false)
  private var _vibratePhone = MutableLiveData<Boolean>(null)
  private var lastScanTime = 0L

  val listId: LiveData<List<String>> = _listId
  val isScanned: LiveData<Boolean> = _isScanned
  val vibratePhone: LiveData<Boolean> = _vibratePhone
  private val _listIngredients = MutableLiveData<List<IngredientsScanned>>(emptyList())
  val listIngredients: LiveData<List<IngredientsScanned>> = _listIngredients

  // We need to scan multiple times the same value to be sure it's not a mistake
  private var previousScan: String? = null
  private var count = 0

  /**
   * Function passed to the ml kit to add the id to the list
   *
   * @param id the id to add
   */
  fun addId(id: String?) {
    Log.v("QrCodeViewModel", "addId: $id , ${System.currentTimeMillis() - lastScanTime}")

    if (id.isNullOrEmpty() || _listId.value!!.contains(id)) return
    if (id.length !in MIN_BARCODE_LENGTH..MAX_BARCODE_LENGTH) return

    if (id == previousScan) {
      count++
      if (count != REQUIRED_SCAN_COUNT) {
        return
      }
    } else {
      previousScan = id
      count = 1
      return
    }

    val list = _listId.value?.toMutableList() ?: mutableListOf()
    list.add(0, id)
    _listId.postValue(list)
    lastScanTime = System.currentTimeMillis()
    _isScanned.postValue(true)
    getIngredients(id)
    _vibratePhone.postValue(!(_vibratePhone.value ?: false))
    Log.v("QrCodeViewModel", "new list: ${_listId.value}")

    CoroutineScope(Dispatchers.IO).launch {
      delay(TIME_COLOR_BORDER_MILLIS)
      withContext(Dispatchers.Main) {
        if (lastScanTime + TIME_COLOR_BORDER_MILLIS < System.currentTimeMillis()) {
          _isScanned.postValue(false)
        }
      }
    }
  }

  fun removeId(id: String) {
    val list = _listId.value?.toMutableList() ?: mutableListOf()
    list.remove(id)
    _listId.postValue(list)
  }

  fun getIngredients(id: String) {
    val list = _listIngredients.value?.toMutableList() ?: mutableListOf()
    viewModelScope.launch(Dispatchers.IO) {
      val ingredient: Ingredient = foodFactsApi.getIngredient(id)
      val nutriments = ingredient.nutritionalInformation.nutrients
      try {
        list.add(
            0,
            IngredientsScanned(
                ingredient.name,
                ingredient.amount,
                0.0,
                nutriments.first { it.nutrientType == "carbohydrates" }.amount,
                nutriments.first { it.nutrientType == "fat" }.amount,
                nutriments.first { it.nutrientType == "protein" }.amount))

        _listIngredients.postValue(list)
      } catch (e: Exception) {
        removeId(id)
        Log.w("QrCodeViewModel", "Error in getting ingredient, surely not a food id: $e")
      }
    }
  }

  fun setIngredients() {
    if (_listIngredients.value.isNullOrEmpty()) return
    recipeRecommendationViewModel.setIngredientList(listIngredients.value!!)
  }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
  ProcessCameraProvider.getInstance(this).also { cameraProvider ->
    cameraProvider.addListener(
        { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
  }
}
