package ge.luka.melodia.media3

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.luka.melodia.common.extensions.toMediaItems
import ge.luka.melodia.domain.model.SongModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayBackManager @Inject constructor(
    @ApplicationContext context: Context, ) {
    private lateinit var mediaController: MediaController

    val currentSongProgress: Float
        get() = mediaController.currentPosition.toFloat() / mediaController.duration.toFloat()

    val currentSongProgressMillis
        get() = mediaController.currentPosition

    init {
        initMediaController(context)
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

    fun playSongAtIndex(index: Int) {
        mediaController.seekTo(index, 0)
    }

    fun seekToPosition(progress: Float) {
        val controller = mediaController
        val songDuration = controller.duration
        controller.seekTo((songDuration * progress).toLong())
    }

    fun seekToPositionMillis(millis: Long) {
        mediaController.seekTo(millis)
    }

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
            { mediaController = mediaControllerFuture.get() },
            MoreExecutors.directExecutor()
        )
    }
}