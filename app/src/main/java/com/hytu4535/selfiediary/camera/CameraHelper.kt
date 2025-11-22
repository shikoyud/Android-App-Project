package com.hytu4535.selfiediary.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraHelper @Inject constructor() {

    var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
        private set

    private var cameraProvider: ProcessCameraProvider? = null
    private var lifecycleOwner: LifecycleOwner? = null
    private var imagePreview: Preview? = null
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        preview: Preview,
        flashMode: Int = ImageCapture.FLASH_MODE_OFF
    ) {
        this.lifecycleOwner = lifecycleOwner
        this.imagePreview = preview

        cameraExecutor = Executors.newSingleThreadExecutor()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                this.cameraProvider = cameraProviderFuture.get()
                imageCapture = ImageCapture
                    .Builder()
                    .setFlashMode(flashMode)
                    .build()
                bindCameraUsecases()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun takePicture(
        context: Context,
        onImageSaved: (savedUri: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val imageCapture = this.imageCapture ?: run {
            onError("Camera not initialized.")
            return
        }

        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + ".jpg"
        val outputDirectory = context.getExternalFilesDir(null)
        if (outputDirectory == null || !outputDirectory.exists()) {
            outputDirectory?.mkdirs()
        }
        val photoFile = File(outputDirectory, fileName)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = photoFile.absolutePath
                    onImageSaved(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError("Lỗi chụp ảnh: ${exception.message}")
                    exception.printStackTrace()
                }
            }


        )
    }

    fun bindCameraUsecases() {
        val provider = cameraProvider ?: return
        val owner = lifecycleOwner ?: return
        val capture = imageCapture ?: return
        val preview = imagePreview ?: return

        provider.unbindAll()
        try {
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(this.lensFacing)
                .build()

            provider.bindToLifecycle(
                owner,
                cameraSelector,
                preview,
                capture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun switchLens(newLensFacing: Int) {
        if (this.lensFacing != newLensFacing) {
            this.lensFacing = newLensFacing
            bindCameraUsecases()
        }
    }

    fun setFlashMode(mode: Int) {
        this.imageCapture?.flashMode = mode
    }

    fun shutdown() {
        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }
    }
}

