package ge.luka.melodia.media3

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayBackService : MediaSessionService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession


    override fun onCreate() {
        super.onCreate()
        player = buildPlayer()
        mediaSession = buildMediaSession()
    }

    private fun buildPlayer(): ExoPlayer {
        return ExoPlayer.Builder(applicationContext)
            .setAudioAttributes(
                AudioAttributes.Builder().setContentType(AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA).build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_ALL
            }
    }

    private fun buildMediaSession(): MediaSession {
        return MediaSession
            .Builder(applicationContext, player)
            .setSessionActivity(buildPendingIntent())
            .build()
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(this, Class.forName("ge.luka.melodia.presentation.MainActivity"))
        intent.action = VIEW_MEDIA_SCREEN_ACTION
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onDestroy() {
        mediaSession.run {
            player.release()
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    companion object {
        const val TAG = "MEDIA_SESSION"
        const val VIEW_MEDIA_SCREEN_ACTION = "MEDIA_SCREEN_ACTION"
    }
}