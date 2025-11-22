package com.hytu4535.selfiediary.ui.capture

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import com.hytu4535.selfiediary.camera.CameraHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CaptureViewModel @Inject constructor(
    val cameraHelper: CameraHelper
) : ViewModel() {

    private val _lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_FRONT)
    val lensFacing: StateFlow<Int> = _lensFacing.asStateFlow()

    private val _flashMode = MutableStateFlow(ImageCapture.FLASH_MODE_OFF)
    val flashMode: StateFlow<Int> = _flashMode.asStateFlow()

    fun toggleCameraLens() {
        val newLens = when (_lensFacing.value) {
            CameraSelector.LENS_FACING_FRONT -> CameraSelector.LENS_FACING_BACK
            else -> CameraSelector.LENS_FACING_FRONT
        }
        _lensFacing.update { newLens }
        cameraHelper.switchLens(newLens)
    }

    fun changeFlashMode() {
        val newMode = when (_flashMode.value) {
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
            else -> ImageCapture.FLASH_MODE_OFF
        }
        _flashMode.update { newMode }
        cameraHelper.setFlashMode(newMode)
    }
}

