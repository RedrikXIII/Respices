package com.example.respices.support.services

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


object LoggerService {
  private var uri: Uri? = null

  // IMPORTANT
  private val isOn: Boolean = false

  fun init(context: Context) {
    if (isOn) {
      val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "respices_log.txt")
        put(MediaStore.Downloads.MIME_TYPE, "text/plain")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
      }

      val resolver = context.contentResolver
      uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

      log("LoggerService: LoggerService initialized", context)
    }
  }

  fun log(message: String, context: Context) {
    if (isOn) {
      try {
        val outputStream = context.contentResolver.openOutputStream(uri!!, "wa") // append mode
        outputStream?.use {
          val formatter = SimpleDateFormat.getTimeInstance()
          val date = Date()
          val current = formatter.format(date)
          it.write("[$current] $message\n".toByteArray())
        }
      } catch (e: Exception) {
        Log.e("logger service error", e.stackTraceToString())
      }
    }
  }
}