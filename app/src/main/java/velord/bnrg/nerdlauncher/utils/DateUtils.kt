package velord.bnrg.nerdlauncher.utils

import java.text.SimpleDateFormat
import java.util.*

fun parseLocalDateTime(value: Date, format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(value)
}