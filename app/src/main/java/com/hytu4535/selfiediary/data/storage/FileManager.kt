package com.hytu4535.selfiediary.data.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val selfieDir: File
        get() = File(context.filesDir, "selfies").apply {
            if (!exists()) mkdirs()
        }

    fun saveSelfie(fileName: String, bytes: ByteArray): String {
        val file = File(selfieDir, fileName)
        FileOutputStream(file).use { it.write(bytes) }
        return file.absolutePath
    }

    fun getSelfieFile(fileName: String): File {
        return File(selfieDir, fileName)
    }

    fun deleteSelfie(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) file.delete() else false
    }

    fun getAllSelfieFiles(): List<File> {
        return selfieDir.listFiles()?.toList() ?: emptyList()
    }
}

