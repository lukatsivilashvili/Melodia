package ge.luka.melodia.common.extensions

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ge.luka.melodia.domain.model.SongModel

const val EXTRA_SONG_ORIGINAL_INDEX = "og"

fun SongModel.toMediaItem(index: Int) =
    MediaItem.Builder()
        .setUri(songPath)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                .setArtist(artist)
                .setAlbumTitle(album)
                .setTitle(title)
                .build()
        )
        .setRequestMetadata(
            MediaItem.RequestMetadata.Builder().setMediaUri(Uri.parse(songPath))
                .setExtras(bundleOf(EXTRA_SONG_ORIGINAL_INDEX to index))
                .build() // to be able to retrieve the URI easily
        )
        .build()

fun List<SongModel>.toMediaItems(startingIndex: Int) = mapIndexed { index, song ->
    song.toMediaItem(startingIndex + index)
}