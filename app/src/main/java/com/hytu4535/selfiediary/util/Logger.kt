package com.hytu4535.selfiediary.util

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    private const val DIR = "reminder_logs"
    private const val FILE_NAME = "reminder_debug.log"

    fun append(context: Context, message: String) {
        try {
            val dir = File(context.getExternalFilesDir(null), DIR)
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, FILE_NAME)
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            FileWriter(file, true).use { fw ->
                fw.appendLine("[$timestamp] $message")
            }
        } catch (e: Exception) {
            // best-effort, ignore
            e.printStackTrace()
        }
    }

    fun getLogPath(context: Context): String {
        val dir = File(context.getExternalFilesDir(null), DIR)
        return File(dir, FILE_NAME).absolutePath
    }
}

