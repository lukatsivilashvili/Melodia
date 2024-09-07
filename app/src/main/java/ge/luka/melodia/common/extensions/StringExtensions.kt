package ge.luka.melodia.common.extensions

fun String.getScreenFromRoute(): String {
    val parts = this.split("(")
    return parts[0].split(".").last()
}