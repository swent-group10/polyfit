package com.github.se.polyfit.viewmodel.qrCode

import android.content.Context
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.ui.screen.IngredientsTMP
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// We need to scan multiple times the same value to be sure it's not a mistake
const val REQUIRED_SCAN_COUNT = 3

// A barcode is between 6 and 13 characters
const val MIN_BARCODE_LENGTH = 6
const val MAX_BARCODE_LENGTH = 13

@HiltViewModel
class BarCodeCodeViewModel @Inject constructor() : ViewModel() {
  private val foodFactsApi = OpenFoodFactsApi()
  private val _listId = MutableLiveData<List<String>>(emptyList())

  val listId: LiveData<List<String>> = _listId
  private val _listIngredients = MutableLiveData<List<IngredientsTMP>>(emptyList())
  val listIngredients: LiveData<List<IngredientsTMP>> = _listIngredients

  // We need to scan multiple times the same value to be sure it's not a mistake
  private var previousScan: String? = null
  private var count = 0

  fun addId(id: String?) {
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
    _listId.value = list
    Log.v("QrCodeViewModel", "new list: ${_listId.value}")
  }

  fun getIngredients() {
    val list = _listIngredients.value?.toMutableList() ?: mutableListOf()
    listId.observeForever { ids ->
      for (code in ids) {
        val ingredient = foodFactsApi.getIngredient(code)
        val nutriments = ingredient.nutritionalInformation.nutrients
        list +=
            IngredientsTMP(
                ingredient.name,
                ingredient.amount,
                0.0,
                nutriments.first { it.nutrientType == "carbohydrates" }.amount,
                nutriments.first { it.nutrientType == "fat" }.amount,
                nutriments.first { it.nutrientType == "protein" }.amount)
      }
      _listIngredients.postValue(list)
    }
  }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
  ProcessCameraProvider.getInstance(this).also { cameraProvider ->
    cameraProvider.addListener(
        { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
  }
}
