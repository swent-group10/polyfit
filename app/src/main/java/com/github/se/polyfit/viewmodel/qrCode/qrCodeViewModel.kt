package com.github.se.polyfit.viewmodel.qrCode

import android.content.Context
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.ArrayList
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class QrCodeViewModel
@Inject
constructor(
): ViewModel() {
  private val _listId = MutableLiveData<List<String>>(emptyList())

  val listId: LiveData<List<String>> = _listId

  fun addId(id: String?) {
    // TODO check if arround 13 characters to be sure not half qrcode
    if (id == null || id.isEmpty() || _listId.value!!.contains(id)) return
    val list = _listId.value?.toMutableList() ?: mutableListOf()
    list.add(0, id)
    _listId.value = list
    Log.v("QrCodeViewModel", "addId: ${_listId.value}")
  }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
        suspendCoroutine { continuation ->
          ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener(
                    { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
          }
        }