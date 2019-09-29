package velord.bnrg.nerdlauncher.utils

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

inline fun View.doOnGlobalLayout(crossinline action: (view: View) -> Unit) {
    val vto = viewTreeObserver
    vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        @SuppressLint("ObsoleteSdkInt")
        @Suppress("DEPRECATION")
        override fun onGlobalLayout() {
            action(this@doOnGlobalLayout)
            when {
                vto.isAlive -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        vto.removeOnGlobalLayoutListener(this)
                    } else {
                        vto.removeGlobalOnLayoutListener(this)
                    }
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                }
            }
        }
    })
}

suspend fun Fragment.updatePhotoView(view: ImageView,
                                     photoFile: File,
                                     width: Int? = null,
                                     height: Int? = null,
                                     contentDescription: Int) = withContext(Dispatchers.Main) {
    if (photoFile.exists()) {
        if (width != null && height != null) {
            val bitmap =
                getScaledBitmap(photoFile.path, width, height)
            view.setImageBitmap(bitmap)
            view.contentDescription =
                getString(contentDescription)
        } else {
            val bitmap =
                getScaledBitmap(photoFile.path, requireActivity())
            view.setImageBitmap(bitmap)
        }
    } else {
        view.setImageDrawable(null)
        view.contentDescription =
            getString(contentDescription)
    }
}




