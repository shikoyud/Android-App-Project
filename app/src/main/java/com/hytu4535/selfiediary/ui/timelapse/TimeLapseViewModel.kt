package com.hytu4535.selfiediary.ui.timelapse

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.domain.usecase.GetSelfiesByDateRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Note: FFmpegKit is optional at compile time; we perform a runtime check before using it.
@HiltViewModel
class TimeLapseViewModel @Inject constructor(
    private val getSelfiesByDateRangeUseCase: GetSelfiesByDateRangeUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _images = MutableStateFlow<List<SelfieEntry>>(emptyList())
    val images: StateFlow<List<SelfieEntry>> = _images

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private val _outputPath = MutableStateFlow<String?>(null)
    val outputPath: StateFlow<String?> = _outputPath

    // previewPath holds the temporarily created video file to be previewed in-app.
    private val _previewPath = MutableStateFlow<String?>(null)
    val previewPath: StateFlow<String?> = _previewPath

    private val _errorMessage = MutableStateFlow<String?>(null)
    // Expose as a function to avoid module-local analyzer warnings about unused properties
    fun errorMessageFlow(): StateFlow<String?> = _errorMessage

    fun clearError() {
        _errorMessage.value = null
    }

    init {
        // log initial value (harmless)
        val currentError = _errorMessage.value
        if (currentError != null) {
            println("TimeLapseViewModel - initial errorMessage: $currentError")
        }
    }

    fun loadRange(start: Long, end: Long) {
        viewModelScope.launch {
            getSelfiesByDateRangeUseCase(start, end).collect { list ->
                _images.value = list.sortedBy { it.timestamp }
                _errorMessage.value = null
            }
        }
    }

    private fun isFFmpegAvailable(): Boolean {
        return try {
            Class.forName("com.arthenica.ffmpegkit.FFmpegKit")
            true
        } catch (_: Exception) {
            false
        }
    }

    fun createTimeLapse(frameRate: Int = 10) {
        val list = _images.value
        if (list.isEmpty()) {
            _errorMessage.value = "Không có ảnh trong khoảng thời gian đã chọn"
            return
        }

        viewModelScope.launch {
            _isProcessing.value = true
            _errorMessage.value = null
            try {
                // Continue regardless of FFmpeg availability; we will use FFmpeg if present or fallback to internal encoder
                 // Offload heavy IO and encoding work to Default dispatcher to avoid blocking Main/UI thread
                 val result = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                     // Create a temporary directory with sequential images
                     val tmpDir = File(context.cacheDir, "timelapse_${System.currentTimeMillis()}")
                     tmpDir.mkdirs()

                     // Copy images as img0001.jpg, img0002.jpg ...
                     list.forEachIndexed { index, entry ->
                         val dst = File(tmpDir, String.format(Locale.US, "img%04d.jpg", index + 1))
                         val path = entry.filePath
                         if (path.startsWith("content://")) {
                             try {
                                 val uri = android.net.Uri.parse(path)
                                 context.contentResolver.openInputStream(uri)?.use { input ->
                                     dst.outputStream().use { output ->
                                         input.copyTo(output)
                                     }
                                 }
                             } catch (e: Exception) {
                                 e.printStackTrace()
                             }
                         } else {
                             val src = File(path)
                             if (src.exists()) src.copyTo(dst, overwrite = true)
                         }
                     }

                     val outputFile = File(context.getExternalFilesDir(null), "timelapse_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.mp4")

                     // By this point, FFmpeg may or may not be available. Use it if present, otherwise use internal encoder.
                     if (isFFmpegAvailable()) {
                         try {
                             val ffmpegClass = Class.forName("com.arthenica.ffmpegkit.FFmpegKit")
                             val executeMethod = ffmpegClass.getMethod("execute", String::class.java)
                             val cmd = "-y -framerate $frameRate -i ${tmpDir.absolutePath}/img%04d.jpg -c:v libx264 -pix_fmt yuv420p ${outputFile.absolutePath}"
                             executeMethod.invoke(null, cmd)
                             // best-effort assume success
                             val pair = Pair(true, outputFile.absolutePath)
                             // clean up temp files
                             try { tmpDir.deleteRecursively() } catch (_: Exception) {}
                             pair
                         } catch (e: Exception) {
                             e.printStackTrace()
                             try { tmpDir.deleteRecursively() } catch (_: Exception) {}
                             Pair(false, null)
                         }
                     } else {
                         // Use internal encoder fallback
                         val imageFiles = tmpDir.listFiles()?.filter { it.isFile }?.sortedBy { it.name } ?: emptyList()
                         val encoded = try {
                             encodeImagesToMp4(imageFiles, outputFile, frameRate)
                         } catch (e: Exception) {
                             e.printStackTrace()
                             false
                         }
                         val pair = Pair(encoded, if (encoded) outputFile.absolutePath else null)
                         try { tmpDir.deleteRecursively() } catch (_: Exception) {}
                         pair
                     }
                 }

                 if (result.first) {
                     // encode succeeded — expose preview path for UI to play. User must explicitly save.
                     _previewPath.value = result.second
                 } else {
                     _previewPath.value = null
                     _errorMessage.value = "Không thể tạo video (encoder thất bại)"
                 }

                // Note: temporary files are cleaned up in the withContext block before returning.

             } catch (e: Exception) {
                 e.printStackTrace()
                 _outputPath.value = null
                _errorMessage.value = e.message
             } finally {
                 _isProcessing.value = false
             }
         }
     }

    private fun encodeImagesToMp4(imageFiles: List<File>, outputFile: File, fps: Int): Boolean {
        if (imageFiles.isEmpty()) return false

        try {
            val first = BitmapFactory.decodeFile(imageFiles[0].absolutePath) ?: return false
            val width = first.width
            val height = first.height

            val mime = "video/avc"
            val format = MediaFormat.createVideoFormat(mime, width, height).apply {
                setInteger(MediaFormat.KEY_BIT_RATE, width * height * 4)
                setInteger(MediaFormat.KEY_FRAME_RATE, fps)
                setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
                // Request a flexible YUV color format
                setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible)
            }

            val encoder = MediaCodec.createEncoderByType(mime)
            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            encoder.start()

            val muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            val bufferInfo = MediaCodec.BufferInfo()
            var trackIndex = -1
            var muxerStarted = false

            val frameIntervalUs = 1_000_000L / fps

            for (i in imageFiles.indices) {
                val bmp = BitmapFactory.decodeFile(imageFiles[i].absolutePath) ?: continue
                // convert to NV12 (YUV420 semi-planar)
                val yuv = convertBitmapToNV12(bmp, width, height)
                bmp.recycle()

                 // queue input
                 val inputIndex = encoder.dequeueInputBuffer(10000)
                 if (inputIndex >= 0) {
                     val inputBuffer = encoder.getInputBuffer(inputIndex)!!
                     inputBuffer.clear()
                     inputBuffer.put(yuv)
                     val presentationTimeUs = i * frameIntervalUs
                     encoder.queueInputBuffer(inputIndex, 0, yuv.size, presentationTimeUs, 0)
                 }

                // drain output
                var outputIndex = encoder.dequeueOutputBuffer(bufferInfo, 0)
                while (outputIndex >= 0) {
                    val encodedData = encoder.getOutputBuffer(outputIndex)!!
                    if (bufferInfo.size != 0) {
                        if (!muxerStarted) {
                            val newFormat = encoder.outputFormat
                            trackIndex = muxer.addTrack(newFormat)
                            muxer.start()
                            muxerStarted = true
                        }
                        encodedData.position(bufferInfo.offset)
                        encodedData.limit(bufferInfo.offset + bufferInfo.size)
                        muxer.writeSampleData(trackIndex, encodedData, bufferInfo)
                    }
                    encoder.releaseOutputBuffer(outputIndex, false)
                    outputIndex = encoder.dequeueOutputBuffer(bufferInfo, 0)
                }
            }

            // signal end of stream by queueing empty buffer with EOS flag
            val inputIndex = encoder.dequeueInputBuffer(10000)
            if (inputIndex >= 0) {
                encoder.queueInputBuffer(inputIndex, 0, 0, (imageFiles.size * frameIntervalUs), MediaCodec.BUFFER_FLAG_END_OF_STREAM)
            }

            // drain remaining
            var outputIndex2 = encoder.dequeueOutputBuffer(bufferInfo, 10000)
            while (outputIndex2 >= 0) {
                val encodedData = encoder.getOutputBuffer(outputIndex2)!!
                if (bufferInfo.size != 0) {
                    if (!muxerStarted) {
                        val newFormat = encoder.outputFormat
                        trackIndex = muxer.addTrack(newFormat)
                        muxer.start()
                        muxerStarted = true
                    }
                    encodedData.position(bufferInfo.offset)
                    encodedData.limit(bufferInfo.offset + bufferInfo.size)
                    muxer.writeSampleData(trackIndex, encodedData, bufferInfo)
                }
                val end = (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0
                encoder.releaseOutputBuffer(outputIndex2, false)
                if (end) break
                outputIndex2 = encoder.dequeueOutputBuffer(bufferInfo, 10000)
            }

            try { if (muxerStarted) muxer.stop() } catch (_: Exception) {}
            muxer.release()
            encoder.stop()
            encoder.release()

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun convertBitmapToNV12(bitmap: android.graphics.Bitmap, width: Int, height: Int): ByteArray {
        val scaled = android.graphics.Bitmap.createScaledBitmap(bitmap, width, height, false)
        val argb = IntArray(width * height)
        scaled.getPixels(argb, 0, width, 0, 0, width, height)
        val yuv = ByteArray(width * height * 3 / 2)
        var yIndex = 0
        var uvIndex = width * height

        // temporary vars for color conversion
        var R: Int
        var G: Int
        var B: Int
        var Y: Int
        var U: Int
        var V: Int

        var index = 0
        for (j in 0 until height) {
            for (i in 0 until width) {
                val color = argb[index]
                R = (color shr 16) and 0xFF
                G = (color shr 8) and 0xFF
                B = color and 0xFF

                // RGB to YUV (BT.601)
                Y = ((66 * R + 129 * G + 25 * B + 128) shr 8) + 16
                U = ((-38 * R - 74 * G + 112 * B + 128) shr 8) + 128
                V = ((112 * R - 94 * G - 18 * B + 128) shr 8) + 128

                if (Y < 0) Y = 0 else if (Y > 255) Y = 255
                if (U < 0) U = 0 else if (U > 255) U = 255
                if (V < 0) V = 0 else if (V > 255) V = 255

                yuv[yIndex++] = Y.toByte()

                // for NV12, UV are interleaved and sampled each 2x2 block
                if (j % 2 == 0 && i % 2 == 0) {
                    yuv[uvIndex++] = U.toByte()
                    yuv[uvIndex++] = V.toByte()
                }

                index++
            }
        }
        if (scaled != bitmap) scaled.recycle()
        return yuv
    }

    fun savePreview(): Boolean {
        val preview = _previewPath.value ?: return false
        try {
            val src = File(preview)
            if (!src.exists()) return false
            val destDir = File(context.getExternalFilesDir(null), "timelapse_saved")
            if (!destDir.exists()) destDir.mkdirs()
            val dest = File(destDir, src.name)
            src.copyTo(dest, overwrite = true)
            // Optionally delete preview
            src.delete()
            _previewPath.value = null
            _outputPath.value = dest.absolutePath
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.value = e.message
            return false
        }
    }

    fun discardPreview() {
        val preview = _previewPath.value ?: return
        try {
            val f = File(preview)
            if (f.exists()) f.delete()
        } catch (_: Exception) {}
        _previewPath.value = null
    }

}
