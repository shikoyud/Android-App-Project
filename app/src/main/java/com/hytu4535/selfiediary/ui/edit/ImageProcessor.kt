package com.hytu4535.selfiediary.ui.edit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import androidx.core.graphics.createBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // FOR CROPPING
    fun createTempImageUri(): Uri {
        val tempFile = File(context.cacheDir, "cropped_output_${System.currentTimeMillis()}.jpg")
        return Uri.fromFile(tempFile)
    }

    fun applyFilterAndSaveTemp(currentImagePath: String, filterName: String): String {
        if (filterName == "None") return currentImagePath

        val originalBitmap = BitmapFactory.decodeFile(currentImagePath)
        if (originalBitmap == null) return currentImagePath

        val filteredBitmap = createFilteredBitmap(originalBitmap, filterName)

        val tempFile = createTempImageFile()

        FileOutputStream(tempFile).use { out ->
            filteredBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        return tempFile.absolutePath
    }
    private fun createFilteredBitmap(originalBitmap: Bitmap, filterName: String): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height
        val outputBitmap = createBitmap(width, height, originalBitmap.config)
        val canvas = Canvas(outputBitmap)
        val paint = Paint()
        val matrix = ColorMatrix()

        when (filterName) {
            "B&W" -> {
                matrix.setSaturation(0f)
            }
            "Sepia" -> {
                val sepiaMatrix = floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
                matrix.set(sepiaMatrix)
            }
            "Vintage" -> {
                matrix.setSaturation(0.5f)
                matrix.setScale(1.1f, 1.1f, 1.1f, 1f)
            }
            "Warm" -> {
                matrix.set(floatArrayOf(
                    1.2f, 0f, 0f, 0f, 0f,
                    0f, 1.0f, 0f, 0f, 0f,
                    0f, 0f, 0.8f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                ))
            }
            "Cool" -> {
                matrix.set(floatArrayOf(
                    0.8f, 0f, 0f, 0f, 0f,
                    0f, 1.0f, 0f, 0f, 0f,
                    0f, 0f, 1.2f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                ))
            }
            else -> { /* None */ }
        }

        paint.colorFilter = ColorMatrixColorFilter(matrix)
        canvas.drawBitmap(originalBitmap, 0f, 0f, paint)
        return outputBitmap
    }
    private fun createTempImageFile(): File {
        return File(context.cacheDir, "filter_temp_${System.currentTimeMillis()}.jpg")
    }

    fun processAndSave(imagePath: String): String? {

        val finalFile = File(context.filesDir, "selfie_${System.currentTimeMillis()}.jpg")

        return try {
            val finalBitmap = BitmapFactory.decodeFile(imagePath)
            if (finalBitmap == null) return null

            FileOutputStream(finalFile).use { out ->
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }
            finalFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}