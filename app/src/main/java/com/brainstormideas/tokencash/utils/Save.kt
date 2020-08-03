package com.brainstormideas.tokencash.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Save {
    private var TheThis: Context? = null
    private val NameOfFolder = "/QRTokenCash"
    private val NameOfFile = "qr_token"
    fun SaveImage(context: Context?, ImageToSave: Bitmap) {
        TheThis = context

        val file_path: String = "Android" + Environment.getDataDirectory().getAbsolutePath().toString() + "/com.brainstormideas.tokencash" + NameOfFolder

        val CurrentDateAndTime = currentDateAndTime
        val dir = File(file_path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "$NameOfFile$CurrentDateAndTime.jpg")
        try {
            val fOut = FileOutputStream(file)
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
            fOut.flush()
            fOut.close()
            MakeSureFileWasCreatedThenMakeAvabile(file)
            AbleToSave()
        } catch (e: FileNotFoundException) {
            UnableToSave()
        } catch (e: IOException) {
            UnableToSave()
        }
    }

    private fun MakeSureFileWasCreatedThenMakeAvabile(file: File) {
        MediaScannerConnection.scanFile(
            TheThis, arrayOf(file.toString()), null
        ) { path, uri -> }
    }

    private val currentDateAndTime: String
        private get() {
            val c: Calendar = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss")
            return df.format(c.getTime())
        }

    private fun UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show()
    }

    private fun AbleToSave() {
        Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show()
    }
}