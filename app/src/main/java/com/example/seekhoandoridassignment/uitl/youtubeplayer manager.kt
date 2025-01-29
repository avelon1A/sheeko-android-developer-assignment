package com.example.seekhoandoridassignment.uitl
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.koin.core.component.KoinComponent

class YouTubePlayerManager(private val context: Context) : KoinComponent {

    private var youTubePlayerView: YouTubePlayerView? = null
    private var currentPlayer1: YouTubePlayer? = null

    fun initializePlayer(lifecycleOwner: LifecycleOwner) {
        if (youTubePlayerView == null) {
            youTubePlayerView = YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)
            }
        }
    }

    fun playVideo(id: String) {
        youTubePlayerView?.apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(currentPlayer: YouTubePlayer) {
                    currentPlayer1 = currentPlayer
                    currentPlayer1?.cueVideo(id, 0f)
                }
            })
        }
    }
    fun resetPlayer(id: String) {
        currentPlayer1?.cueVideo(id,0f)
    }

    fun getPlayerView(): YouTubePlayerView? {
        return youTubePlayerView
    }
}
