package com.example.matheasyapp.view.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File

object ExifUtil {
    fun getRotatedBitmap(file: File): Bitmap {
        // Đọc ảnh từ file
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)

        // Lấy thông tin độ xoay từ EXIF metadata
        val exif = ExifInterface(file.absolutePath)
        val rotation = when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        // Xoay ảnh nếu cần
        return rotateBitmap(bitmap, rotation)
    }

    private fun rotateBitmap(bitmap: Bitmap, rotation: Int): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotation.toFloat())
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
