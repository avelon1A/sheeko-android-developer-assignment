package com.example.seekhoandoridassignment.di

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import com.example.seekhoandoridassignment.data.network.AnimeRepositoryImp
import com.example.seekhoandoridassignment.data.network.ApiService
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import com.example.seekhoandoridassignment.presntation.screens.SplashScreen
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import com.example.seekhoandoridassignment.presntation.viewmodels.MangaViewModel
import com.example.seekhoandoridassignment.presntation.viewmodels.SplashViewModel
import com.example.seekhoandoridassignment.uitl.YouTubePlayerManager
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val  appModule = module {
 single { provideConverterFactory() }
 single { androidApplication()}
 single { provideRetrofit(get()) }
 single { provideService(get()) }
 single { provideAnimeRepository(get()) }
  single { YouTubePlayerManager(get()) }
 viewModel { HomeViewModel(get(),get(),get()) }
 viewModel { MangaViewModel(get()) }
 viewModel { SplashViewModel(get()) }
 single {
  ImageLoader.Builder(get<Context>())
   .memoryCachePolicy(CachePolicy.ENABLED)
   .diskCachePolicy(CachePolicy.ENABLED)
   .crossfade(true)
   .build()
 }


}

fun provideConverterFactory(): GsonConverterFactory =
 GsonConverterFactory.create()


fun provideRetrofit(gsonConverterFactory: GsonConverterFactory
): Retrofit {
 return Retrofit.Builder()
  .baseUrl("https://api.jikan.moe/v4/")
  .addConverterFactory(gsonConverterFactory)
  .build()
}

fun provideService(retrofit: Retrofit): ApiService =
 retrofit.create(ApiService::class.java)

fun provideAnimeRepository(apiService: ApiService): AnimeRepository  = AnimeRepositoryImp(apiService)
