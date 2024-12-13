package ge.luka.melodia.common.transformers

import ge.luka.melodia.data.database.model.SongModelEntity
import ge.luka.melodia.domain.model.SongModel

fun SongModel.toEntity(): SongModelEntity {
    return SongModelEntity(
        songId = songId,
        albumId = albumId,
        artistId = artistId,
        title = title,
        album = album,
        artist = artist,
        duration = duration,
        songPath = songPath,
        artUri = artUri,
        bitrate = bitrate
    )
}

fun SongModelEntity.toDomain(): SongModel {
    return SongModel(
        songId = songId,
        albumId = albumId,
        artistId = artistId,
        title = title,
        album = album,
        artist = artist,
        duration = duration,
        songPath = songPath,
        artUri = artUri,
        bitrate = bitrate
    )
}

fun List<SongModelEntity>.toDomain(): List<SongModel> {
    return this.map {it.toDomain()}
}