package ge.luka.melodia.media3

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.luka.melodia.common.extensions.toMediaItems
import ge.luka.melodia.common.extensions.toSongModel
import ge.luka.melodia.domain.model.MediaPlayerState
import ge.luka.melodia.domain.model.PlaybackState
import ge.luka.melodia.domain.model.PlayerState
import ge.luka.melodia.domain.model.RepeatMode
import ge.luka.melodia.domain.model.SongModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayBackManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    private lateinit var mediaController: MediaController

    init {
        initMediaController(context)
    }

    val currentSongProgress: Float
        get() = if (::mediaController.isInitialized && mediaController.duration > 0) {
            mediaController.currentPosition.toFloat() / mediaController.duration.toFloat()
        } else {
            0f // Return 0 progress if mediaController is not initialized
        }
    val currentSongProgressMillis
        get() = if (::mediaController.isInitialized && mediaController.duration > 0) {
            mediaController.currentPosition
        } else {
            0L // Return 0 progress if mediaController is not initialized
        }

    private val _state = MutableStateFlow(MediaPlayerState.empty)

    val state: StateFlow<MediaPlayerState>
        get() = _state

    private val playbackState: PlayerState
        get() {
            return when (mediaController.playbackState) {
                Player.STATE_READY -> {
                    if (mediaController.playWhenReady) PlayerState.PLAYING
                    else PlayerState.PAUSED
                }

                Player.STATE_BUFFERING -> PlayerState.BUFFERING
                else -> PlayerState.PAUSED
            }
        }


    /**
     *  Updates the state of the player
     */
    fun updateState() {
        val currentMediaItem = mediaController.currentMediaItem
        val song = currentMediaItem?.toSongModel()
        val playBackState = PlaybackState(
            playbackState,
            mediaController.shuffleModeEnabled,
            RepeatMode.NO_REPEAT
        )
        _state.value = MediaPlayerState(song, playBackState)
    }

    /**
     * Changes the current playlist of the player and starts playing the song at the specified index
     */
    fun setPlaylistAndPlayAtIndex(playlist: List<SongModel>, index: Int = 0) {
        if (playlist.isEmpty()) return
        val mediaItems = playlist.toMediaItems(0)
        stopPlayback() // release everything
        mediaController.apply {
            setMediaItems(mediaItems, index, 0)
            prepare()
            play()
        }
    }

    /**
     * Shuffles the current playlist and starts playing the first song
     */
    fun shufflePlaylist(playlist: List<SongModel>) {
        if (playlist.isEmpty()) return
        val shuffled = playlist.shuffled()
        stopPlayback()
        mediaController.apply {
            setMediaItems(shuffled.toMediaItems(0), 0, 0)
            prepare()
            play()
        }
    }

    /**
     * Toggle the player state
     */
    fun togglePlayback() {
        mediaController.prepare()
        mediaController.playWhenReady = !mediaController.playWhenReady
    }

    /**
     * Jumps to the next song in the queue
     */
    fun playNextSong() {
        mediaController.prepare()
        mediaController.seekToNext()
    }

    /**
     * Jumps to the previous song in the queue
     */
    fun playPreviousSong() {
        mediaController.prepare()
        mediaController.seekToPrevious()
    }

    /**
     *  Seek to a specific position in the current song
     */
    fun seekToPosition(progress: Float) {
        val songDuration = mediaController.duration
        mediaController.seekTo((songDuration * progress).toLong())
    }

    /**
     *  Stops the playback (Releases all resources)
     */
    private fun stopPlayback() {
        mediaController.stop()
    }

    private fun initMediaController(context: Context) {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlayBackService::class.java))
        val mediaControllerFuture = MediaController.Builder(context, sessionToken)
            .setApplicationLooper(context.mainLooper)
            .buildAsync()

        mediaControllerFuture.addListener(
            {
                try {
                    mediaController = mediaControllerFuture.get()
                    attachListeners()
                } catch (e: Exception) {
                    e.printStackTrace() // Log the error if initialization fails
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun attachListeners() {
        mediaController.addListener(object : Player.Listener {

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateState()
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                updateState()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateState()
            }
        })
    }

}