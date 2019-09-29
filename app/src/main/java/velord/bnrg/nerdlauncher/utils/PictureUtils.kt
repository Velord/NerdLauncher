package velord.bnrg.nerdlauncher.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getScaledBitmap(path: String, destWith: Int, destHeight: Int): Bitmap =
    withContext(Dispatchers.IO) {
    // Read in the dimensions of the image on disk
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()
    // Figure out how much to scale down by
    var inSampleSize = 1
    if (srcHeight > destHeight || srcWidth > destWith) {
        val heightScale = srcHeight / destHeight
        val widthScale = srcWidth / destWith

        val sampleScale = if (heightScale > widthScale)
            heightScale
        else
            widthScale

        inSampleSize = Math.round(sampleScale)
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize
    // Read in and create final bitmap
     BitmapFactory.decodeFile(path, options)
}

suspend fun getScaledBitmap(path: String, activity: Activity): Bitmap =
    withContext(Dispatchers.IO) {
        val size = Point()
        activity.windowManager.defaultDisplay.getSize(size)

        getScaledBitmap(path, size.x, size.y)
    }