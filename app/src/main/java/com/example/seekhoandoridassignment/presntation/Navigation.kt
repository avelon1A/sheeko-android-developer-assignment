package com.example.seekhoandoridassignment.presntation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.seekhoandoridassignment.presntation.screens.HomeScreen
import com.example.seekhoandoridassignment.presntation.screens.MangaPage
import com.example.seekhoandoridassignment.presntation.screens.SearchScreen
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val homeViewModel: HomeViewModel = koinViewModel()

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = HomeScreen,
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            composable<HomeScreen> {
                HomeScreen(navController ,homeViewModel)
            }
            composable<MangaPage> {
                MangaPage( )
            }
            composable<SearchScreen> {
                SearchScreen(navController,homeViewModel)
            }

        }
    }
}