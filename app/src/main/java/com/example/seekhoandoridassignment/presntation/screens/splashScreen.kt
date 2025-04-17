package com.example.seekhoandoridassignment.presntation.screens


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.seekhoandoridassignment.presntation.viewmodels.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = koinViewModel()
    // Start a coroutine to load the image during splash screen
    val imageDownloaded =  viewModel.imageDownloaded.collectAsState().value
    val topThree = viewModel.topThree.collectAsState().value
    LaunchedEffect(imageDownloaded) {
        if (imageDownloaded) {
            downloadImagesAndNavigate(navController, topThree)
        }
    }

    // Splash Screen UI
    Box(
        modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

suspend fun downloadImagesAndNavigate(
    navController: NavController,
    topThree: List<String> // Assuming this is a list of image URLs
) {
    val imageLoader = ImageLoader.Builder(navController.context).build()

    try {
        for ((index, imageUrl) in topThree.withIndex()) {
            val request = ImageRequest.Builder(navController.context)
                .data(imageUrl)
                .build()

            val result = imageLoader.execute(request)

            if (result.drawable != null) {
                val bitmap = result.drawable?.toBitmap()
                saveImageLocally(navController.context, bitmap, "splash_image_$index.png")
            } else {
                Log.e("SplashScreen", "Failed to load image at index $index: $imageUrl")
            }
        }

        // ✅ All images loaded, now navigate
        navController.navigate("home_screen") // Replace with your actual destination
    } catch (e: Exception) {
        Log.e("SplashScreen", "Error downloading images: ${e.message}")
    }
}

fun saveImageLocally(context: Context, bitmap: Bitmap?, fileName: String) {
    bitmap?.let {
        try {
            // Get the internal storage directory (private storage)
            val directory = context.filesDir // This is private to the app

            // Create the file where the image will be saved
            val file = File(directory, fileName)

            // Create an output stream to write the image to the file
            val outStream = FileOutputStream(file)

            // Compress the image to the output stream
            it.compress(Bitmap.CompressFormat.PNG, 100, outStream)

            // Close the output stream
            outStream.flush()
            outStream.close()

            // Log the successful saving of the image
            Log.d("SplashScreen", "Image saved locally: ${file.absolutePath}")

        } catch (e: IOException) {
            Log.e("SplashScreen", "Error while saving image: ${e.message}")
        }
    }
}
