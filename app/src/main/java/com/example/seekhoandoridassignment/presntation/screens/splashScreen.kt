package com.example.seekhoandoridassignment.presntation.screens


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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
@Composable
fun SplashScreen(navController: NavController) {
    // Start a coroutine to load the image during splash screen
    LaunchedEffect(Unit) {
        // Simulate image download during splash screen
        downloadImageAndNavigate(navController)
    }

    // Splash Screen UI
    Box(
        modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Black),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

suspend fun downloadImageAndNavigate(navController: NavController) {
    // Use Coil to download the image
    val imageUrl = "https://i.sstatic.net/7bPhP.jpg"
    val imageLoader = ImageLoader.Builder(navController.context).build()

    try {
        // Create an image request to load the image
        val request = ImageRequest.Builder(navController.context)
            .data(imageUrl)
            .build()

        // Use Coil to fetch the image
        val result = imageLoader.execute(request)

        if (result.drawable != null) {
            // The image is loaded successfully
            val bitmap = result.drawable?.toBitmap()

            // Save the image locally (Optional, if you want to store the image)
            saveImageLocally(navController.context, bitmap, "splash_image.png")

            // After downloading the image, navigate to the next screen
            navController.navigate(HomeScreen) // Make sure to replace with your actual destination
        } else {
            Log.e("SplashScreen", "Failed to load image.")
        }
    } catch (e: Exception) {
        Log.e("SplashScreen", "Error while downloading image: ${e.message}")
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
