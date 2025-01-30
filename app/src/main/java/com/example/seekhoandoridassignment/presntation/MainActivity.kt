package com.example.seekhoandoridassignment.presntation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.seekhoandoridassignment.presntation.ui.theme.SeekhoAndoridAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            SeekhoAndoridAssignmentTheme {
                Scaffold ( ){ innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                    )
                }

            }
        }
    }
}

