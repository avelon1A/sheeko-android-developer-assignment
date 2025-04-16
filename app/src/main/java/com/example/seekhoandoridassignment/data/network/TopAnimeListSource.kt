package com.example.seekhoandoridassignment.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.seekhoandoridassignment.data.dto.Anime
import com.example.seekhoandoridassignment.domain.repository.AnimeRepository

class TopAnimeListSource(val repository: AnimeRepository): PagingSource<Int, Anime>() {
    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        return try {
            val nextPage = params.key ?: 1
            val animeList = repository.getTopAnime(nextPage, filter = "airing")

            LoadResult.Page(
                data = animeList.animeList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)

        }
    }
}