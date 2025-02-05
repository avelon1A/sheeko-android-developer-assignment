package com.example.seekhoandoridassignment.presntation

import android.content.Context
import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.seekhoandoridassignment.presntation.screens.HomeScreen
import com.example.seekhoandoridassignment.presntation.screens.MangaPage
import com.example.seekhoandoridassignment.presntation.ui.theme.SeekhoAndoridAssignmentTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            val context = LocalContext.current
            val isTouchExplorationEnabled = remember {
                val am = context.getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
                am.isEnabled && am.isTouchExplorationEnabled
            }
            val navController = rememberNavController()
            SeekhoAndoridAssignmentTheme {
                val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

                Scaffold (
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    bottomBar = {
                        BottomAppBar(
                            scrollBehavior = if (!isTouchExplorationEnabled) scrollBehavior else null,
                            content = {
                                IconButton(onClick = { navController.navigate(HomeScreen) }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Localized description"
                                    )
                                }
                                IconButton(onClick = { navController.navigate(MangaPage) }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Localized description"
                                    )
                                }
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.Check, contentDescription = "Localized description")
                                }
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Localized description")
                                }
                            }
                        )
                    }
                ){ innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),navController
                    )
                }

            }
        }
    }
}

