package com.dboy.rickandmortyapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.api.response.episode.Episode
import com.dboy.rickandmortyapp.util.CHARACTERS_STARTING_PAGE

class EpisodesPagingSource(
    private val rmApi: RickAndMortyAPI
) : PagingSource<Int, Episode>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        val page = params.key ?: CHARACTERS_STARTING_PAGE
        Log.i("EpisodesPaging", "dentro do load")

        return loadResponse {
            rmApi.getEpisodes(page)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        return state.anchorPosition
    }
}