package ge.luka.melodia.domain.model


enum class RepeatMode {
    REPEAT_ALL, REPEAT_SONG, NO_REPEAT;

    fun next() = when (this) {
        REPEAT_ALL -> REPEAT_SONG
        REPEAT_SONG -> NO_REPEAT
        NO_REPEAT -> REPEAT_ALL
    }
}