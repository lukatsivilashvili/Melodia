package ge.luka.melodia.common.extensions

import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.formatDuration(): String {
    val minutes = TimeUnit.MINUTES.convert(this, TimeUnit.MILLISECONDS)
    val seconds = TimeUnit.SECONDS.convert(
        this,
        TimeUnit.MILLISECONDS
    ) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    return String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds)
}

fun Long.formatAlbumDuration(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    return "${minutes}min"
}