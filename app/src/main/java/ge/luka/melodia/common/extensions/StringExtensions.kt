package ge.luka.melodia.common.extensions

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.getScreenFromRoute(): String {
    val parts = this.split("(")
    return parts[0].split(".").last()
}

fun String.trimAlbumTitle(): String {
    return this
        .split(Regex("\\s*[^a-zA-Z\\s]"))
        .firstOrNull()
        ?.replace(Regex("\\s+feat\\.?.*$", RegexOption.IGNORE_CASE), "")
        ?.trim()
        ?: ""
}

fun String.toComposeColor(): Color {
    // parseColor returns an ARGB Int
    val argb = this.toColorInt()
    // then pass it straight into Compose’s Color(Int) constructor
    return Color(argb)
}