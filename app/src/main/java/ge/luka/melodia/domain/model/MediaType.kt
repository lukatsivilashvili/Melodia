package ge.luka.melodia.domain.model


sealed class MediaType {
    data object Song : MediaType()
    data object Album : MediaType()
}