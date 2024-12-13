package ge.luka.melodia.common.transformers

import ge.luka.melodia.data.database.model.ArtistModelEntity
import ge.luka.melodia.domain.model.ArtistModel

fun ArtistModel.toEntity(): ArtistModelEntity {
    return ArtistModelEntity(
        artistId = artistId,
        title = title,
        artUri = artUri
    )
}

fun ArtistModelEntity.toDomain(): ArtistModel {
    return ArtistModel(
        artistId = artistId,
        title = title,
        artUri = artUri
    )
}

fun List<ArtistModelEntity>.toDomain(): List<ArtistModel> {
    return this.map {it.toDomain()}
}