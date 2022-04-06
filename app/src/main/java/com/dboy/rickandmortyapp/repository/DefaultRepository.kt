package com.dboy.rickandmortyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.api.response.character.Character
import com.dboy.rickandmortyapp.api.response.episode.Episode
import com.dboy.rickandmortyapp.paging.CharactersPagingSource
import com.dboy.rickandmortyapp.paging.EpisodesPagingSource
import com.dboy.rickandmortyapp.paging.FilterCharactersPagingSource
import com.dboy.rickandmortyapp.util.Resource
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val rmApi: RickAndMortyAPI,
) {
    fun getCharactersWithPagination(
        nameQuery: String = "",
    ): LiveData<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharactersPagingSource(rmApi, nameQuery)
            }
        ).liveData
    }

    fun getFilteredCharactersWithPagination(
        nameQuery: String = "",
        statusQuery: String = "",
        genderQuery: String = ""
    ): LiveData<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, prefetchDistance = 4, enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FilterCharactersPagingSource(rmApi, nameQuery, statusQuery, genderQuery)
            }
        ).liveData
    }

    suspend fun getSingleCharacter(id: Int): Resource<Character> {
        return try {
            val response = rmApi.getSingleCharacter(id)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(message = response.message(), result)
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    fun getEpisodesWithPagination(): LiveData<PagingData<Episode>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, prefetchDistance = 4, enablePlaceholders = false
            ),
            pagingSourceFactory = {
                EpisodesPagingSource(rmApi)
            }
        ).liveData
    }
}