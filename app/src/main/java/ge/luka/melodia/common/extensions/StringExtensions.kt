package ge.luka.melodia.common.extensions

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