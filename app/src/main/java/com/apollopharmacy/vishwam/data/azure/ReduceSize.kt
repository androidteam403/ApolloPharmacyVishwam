package com.apollopharmacy.vishwam.data.azure

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

object ReduceSize {
    @RequiresApi(Build.VERSION_CODES.N)
    fun reduceImageSize(imageFile: File) {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true // Load only metadata to avoid memory leak
            BitmapFactory.decodeFile(imageFile.absolutePath, options)
            options.inSampleSize = Integer.max(options.outWidth / 500, options.outHeight / 500)
            val ins = imageFile.inputStream()
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeStream(ins, null, options)
            ins.close()
            val ops = imageFile.outputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 85, imageFile.outputStream())
            ops.close()
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
    }
}