package com.example.seekhoandoridassignment.di

import com.example.seekhoandoridassignment.BuildConfig.BASE_URL
import com.example.seekhoandoridassignment.data.network.AnimeRepositoryImp
import com.example.seekhoandoridassignment.data.network.ApiService
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository
import com.example.seekhoandoridassignment.presntation.viewmodels.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val  appModule = module {
 single { provideConverterFactory() }
 single { provideRetrofit(get()) }
 single { provideService(get()) }
 single { provideAnimeRepository(get()) }
 viewModel { HomeViewModel(get()) }


}

fun provideConverterFactory(): GsonConverterFactory =
 GsonConverterFactory.create()


fun provideRetrofit(gsonConverterFactory: GsonConverterFactory
): Retrofit {
 return Retrofit.Builder()
  .baseUrl(BASE_URL)
  .addConverterFactory(gsonConverterFactory)
  .build()
}

fun provideService(retrofit: Retrofit): ApiService =
 retrofit.create(ApiService::class.java)

fun provideAnimeRepository(apiService: ApiService): AnimeRepository  = AnimeRepositoryImp(apiService)