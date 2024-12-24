package com.global.coffer.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.InputStream
import java.io.OutputStream


fun saveFile(context: Context, file: File, mimeType: String): Uri? {
    var folderName = "Pictures"
    var extension = ".jpg"
    when (mimeType) {
        "gif" -> {
            extension = ".gif"
        }
        "image" -> {
            extension = ".jpg"
        }
        "video" -> {
            extension = ".mp4"
            folderName = "Movies"
        }
        "audio" -> {
            extension = ".mp3"
            folderName = "Music"
        }
        "pdf" -> {
            extension = ".pdf"
            folderName = "Documents"
        }
    }
    var uri: Uri? = null
    val values = ContentValues()
    val fileName = "file_" + System.currentTimeMillis() + extension
    if (Build.VERSION.SDK_INT >= 29) {
        values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
        values.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Files.FileColumns.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, folderName)
        values.put(MediaStore.Files.FileColumns.IS_PENDING, true)
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
        when {
            mimeType.contains("image") -> {
                uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
            mimeType.contains("video") -> {
                uri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            }
            mimeType.contains("audio") -> {
                uri = context.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
            }
            mimeType.contains("pdf") -> {
                uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
            }
        }
    } else {
        values.put(MediaStore.Files.FileColumns.TITLE, fileName)
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
        values.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
        if (mimeType.contains("image")) {
            values.put(MediaStore.Files.FileColumns.MIME_TYPE, "image")
            uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else if (mimeType.contains("video")){
            values.put(MediaStore.Files.FileColumns.MIME_TYPE, "video")
            uri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        }
    }
    uri?.apply {
        runCatching {
            saveFileToStream(context.contentResolver.openInputStream(Uri.fromFile(file))!!,context.contentResolver.openOutputStream(uri)!!)
            values.put(MediaStore.Video.Media.IS_PENDING, false)
            context.contentResolver.update(this, values, null, null)
            return this
        }.getOrElse {
            return null
        }
    }
    return uri
}


private fun saveFileToStream(input: InputStream, outputStream: OutputStream) {
    input.use {
        runCatching {
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
    }
}





