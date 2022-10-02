package com.brohit.plantdoctor.common

import android.graphics.Bitmap
import android.graphics.Matrix
import kotlin.math.abs

fun Bitmap.getResizedBitmap(newWidth: Int, newHeight: Int): Bitmap {
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    val resizedBitmap = Bitmap.createBitmap(
        this, 0, 0, width, height, matrix, false
    )
    recycle()
    return resizedBitmap
}

fun Bitmap.getResizedBitmap(): Bitmap? {
    val narrowSize = width.coerceAtMost(height)
    val differ = abs((height - width) / 2.0f).toInt()
    width = if (width == narrowSize) 0 else differ
    height = if (width == 0) differ else 0
    val resizedBitmap = Bitmap.createBitmap(this, width, height, narrowSize, narrowSize)
    recycle()
    return resizedBitmap
}