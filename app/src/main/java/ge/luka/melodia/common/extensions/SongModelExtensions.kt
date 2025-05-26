package ge.luka.melodia.common.extensions

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.core.net.toUri
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
const val PALETTE = "palette"

@OptIn(UnstableApi::class)
fun SongModel.toMediaItem(index: Int): MediaItem =
    MediaItem.Builder()
        .setUri(songPath?.toUri())
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                .setArtist(artist)
                .setAlbumTitle(album)
                .setTitle(title)
                .setArtworkUri(artUri?.toUri())
                .setDurationMs(duration)
                .build()
        )
        .setRequestMetadata(
            MediaItem.RequestMetadata.Builder()
                .setMediaUri(songPath?.toUri())
                .setExtras(Bundle().apply {
                    putInt(EXTRA_SONG_ORIGINAL_INDEX, index)
                    putString(ART_URI, artUri)
                    putLong(DURATION, duration ?: 0)
                    putLong(SONG_ID, songId ?: 0)
                    putLong(ALBUM_ID, albumId ?: 0)
                    putLong(ARTIST_ID, artistId ?: 0)
                    putInt(BITRATE, bitrate ?: 0)
                    // create a nested Bundle for the palette
                    val paletteBundle = Bundle().apply {
                        palette?.forEach { (key, value) ->
                            putString(key, value)
                        }
                    }
                    putBundle(PALETTE, paletteBundle)
                })
                .build()
        )
        .build()

fun MediaItem.toSongModel(): SongModel {
    val extras = requestMetadata.extras

    // retrieve the nested Bundle and rebuild the Map<String, String>
    val paletteMap: Map<String, String>? = extras
        ?.getBundle(PALETTE)
        ?.let { bundle ->
            bundle.keySet().associateWith { k ->
                bundle.getString(k).orEmpty()
            }
        }

    return SongModel(
        songPath = requestMetadata.mediaUri.toString(),
        title    = mediaMetadata.title.toString(),
        artist   = mediaMetadata.artist?.toString().orEmpty(),
        album    = mediaMetadata.albumTitle?.toString().orEmpty(),
        songId   = extras?.getLong(SONG_ID, 0L) ?: 0L,
        albumId  = extras?.getLong(ALBUM_ID, 0L) ?: 0L,
        artistId = extras?.getLong(ARTIST_ID, 0L) ?: 0L,
        artUri   = extras?.getString(ART_URI).orEmpty(),
        duration = extras?.getLong(DURATION, 0L) ?: 0L,
        bitrate  = extras?.getInt(BITRATE, 0) ?: 0,
        palette  = paletteMap
    )
}

fun List<SongModel>.toMediaItems(startingIndex: Int) = mapIndexed { index, song ->
    song.toMediaItem(startingIndex + index)
}

fun List<SongModel>.sortedByTrackNumber(): List<SongModel> {
    return sortedWith(compareBy {
        // If trackNumber is 1000 or above, subtract 1000 so that 1001 becomes 1, etc.
        if (it.trackNumber!! >= 1000) it.trackNumber - 1000 else it.trackNumber
    })
}