package ge.luka.melodia.common.extensions

import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import ge.luka.melodia.domain.model.SongModel

const val EXTRA_SONG_ORIGINAL_INDEX = "og"
const val ART_URI = "art_uri"
const val DURATION = "duration"
const val SONG_ID = "song_id"
const val ALBUM_ID = "album_id"
const val ARTIST_ID = "artist_id"
const val BITRATE = "bitrate"

@OptIn(UnstableApi::class)
fun SongModel.toMediaItem(index: Int) =
    MediaItem.Builder()
        .setUri(songPath)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                .setArtist(artist)
                .setAlbumTitle(album)
                .setTitle(title)
                .setArtworkUri(Uri.parse(artUri))
                .setDurationMs(duration)
                .build()
        )
        .setRequestMetadata(
            MediaItem.RequestMetadata.Builder().setMediaUri(Uri.parse(songPath))
                .setExtras(
                    bundleOf(
                        EXTRA_SONG_ORIGINAL_INDEX to index,
                        ART_URI to artUri,
                        DURATION to duration,
                        SONG_ID to songId,
                        ALBUM_ID to albumId,
                        ARTIST_ID to artistId,
                        BITRATE to bitrate
                    )
                )
                .build() // to be able to retrieve the URI easily
        )
        .build()

fun List<SongModel>.toMediaItems(startingIndex: Int) = mapIndexed { index, song ->
    song.toMediaItem(startingIndex + index)
}

fun MediaItem.toSongModel(): SongModel {
    return SongModel(
        songPath = requestMetadata.mediaUri.toString(),
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.artist?.toString() ?: "",
        album = mediaMetadata.albumTitle?.toString() ?: "",
        songId = requestMetadata.extras?.getLong(SONG_ID, 0L),
        albumId = requestMetadata.extras?.getLong(ALBUM_ID, 0L),
        artistId = requestMetadata.extras?.getLong(ARTIST_ID, 0L),
        artUri = requestMetadata.extras?.getString(ART_URI, ""),
        duration = requestMetadata.extras?.getLong(DURATION, 0L),
        bitrate = requestMetadata.extras?.getInt(BITRATE, 0),
    )
}