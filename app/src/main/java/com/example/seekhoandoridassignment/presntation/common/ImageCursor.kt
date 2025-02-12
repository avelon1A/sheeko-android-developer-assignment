package com.example.seekhoandoridassignment.presntation.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seekhoandoridassignment.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ImageCarousel(imageList: List<Int>) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var imageOffset by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    val nextImage = {
        coroutineScope.launch {
            imageOffset = -1f
            delay(200)
            currentIndex = (currentIndex + 1) % imageList.size
            imageOffset = 1f
        }
    }

    val prevImage = {
        coroutineScope.launch {
            imageOffset = 1f
            delay(200)
            currentIndex = (currentIndex - 1 + imageList.size) % imageList.size
            imageOffset = -1f
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(bottom = 10.dp, top = 10.dp),
        contentAlignment = Alignment.Center
    ) {


        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth })
                } else {
                    slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth })
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { index ->
            Image(
                painter = painterResource(imageList[index]),
                contentDescription = "carousel image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(
            onClick = { prevImage() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Image",tint = Color.White)
        }

        IconButton(
            onClick = { nextImage() },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Image",tint = Color.White)
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            imageList.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (index == currentIndex) Color.White else Color.Gray)
                )
            }
        }
    }
}
@Preview
@Composable
fun CursorLiablePreview()
{
    ImageCarousel(
        imageList = listOf(
            R.drawable.sololeveling,
            R.drawable.demo,
            R.drawable.applogo,
        )
    )
}