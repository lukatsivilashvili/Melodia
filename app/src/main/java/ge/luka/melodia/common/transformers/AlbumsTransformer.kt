package ge.luka.melodia.common.transformers

import ge.luka.melodia.data.database.model.AlbumModelEntity
import ge.luka.melodia.domain.model.AlbumModel

fun AlbumModel.toEntity(): AlbumModelEntity {
    return AlbumModelEntity(
        albumId = albumId,
        artistId = artistId,
        title = title,
        artist = artist,
        songCount = songCount,
        duration = duration,
        artUri = artUri,
    )
}

fun AlbumModelEntity.toDomain(): AlbumModel {
    return AlbumModel(
        albumId = albumId,
        artistId = artistId,
        title = title,
        artist = artist,
        songCount = songCount,
        duration = duration,
        artUri = artUri,
    )
}

fun List<AlbumModelEntity>.toDomain(): List<AlbumModel> {
    return this.map {it.toDomain()}
}