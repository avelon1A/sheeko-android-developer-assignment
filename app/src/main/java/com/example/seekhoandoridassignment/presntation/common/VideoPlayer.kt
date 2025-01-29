package com.example.seekhoandoridassignment.presntation.common
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.example.seekhoandoridassignment.uitl.YouTubePlayerManager
import org.koin.compose.getKoin

@Composable
fun VideoPlayer(id: String, modifier: Modifier, lifecycleOwner: LifecycleOwner) {
    val youTubePlayerManager: YouTubePlayerManager = getKoin().get()

    var isPlayerReady by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        youTubePlayerManager.resetPlayer(id)
        youTubePlayerManager.initializePlayer(lifecycleOwner)
        youTubePlayerManager.playVideo(id)
        isPlayerReady = false
        isPlayerReady = true

    }

    AnimatedVisibility(
        visible = isPlayerReady,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            )),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                youTubePlayerManager.getPlayerView()!!
            }

        )
    }
}
