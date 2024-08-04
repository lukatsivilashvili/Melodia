package ge.luka.melodia.common.extensions

fun String.getScreenFromRoute(): String {
    return this.split(".").last()
}