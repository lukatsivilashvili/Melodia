package ge.luka.melodia.common.utils

import android.util.Base64

object Base64Helper {
    fun encodeToBase64(input: String): String {
        return Base64.encodeToString(input.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
    }

    fun decodeFromBase64(encoded: String): String {
        val decodedBytes = Base64.decode(encoded, Base64.DEFAULT)
        return String(decodedBytes, Charsets.UTF_8)
    }
}