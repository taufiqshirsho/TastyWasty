package com.taufiq.tastywasty.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun copyUriToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
