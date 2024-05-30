package com.github.se.polyfit.viewmodel.qrCode

import android.content.Context
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

@HiltViewModel
class BarCodeCodeViewModel @Inject constructor() : ViewModel() {
  private val _listId = MutableLiveData<List<String>>(emptyList())
  val _isScanned = MutableLiveData(false)
  private var lastScanTime = System.currentTimeMillis()

  val listId: LiveData<List<String>> = _listId
  val isScanned: LiveData<Boolean> = _isScanned

  // We need to scan multiple times the same value to be sure it's not a mistake
  private var previousScan: String? = null
  private var count = 0

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
    _listId.value = list
    lastScanTime = System.currentTimeMillis()
    _isScanned.value = true
    Log.v("QrCodeViewModel", "new list: ${_listId.value}")

    CoroutineScope(Dispatchers.IO).launch {
      delay(TIME_COLOR_BORDER_MILLIS)
      withContext(Dispatchers.Main) {
        if (lastScanTime + TIME_COLOR_BORDER_MILLIS < System.currentTimeMillis()) {
          _isScanned.value = false
        }
      }
    }
  }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
  ProcessCameraProvider.getInstance(this).also { cameraProvider ->
    cameraProvider.addListener(
        { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
  }
}
