package com.dboy.rickandmortyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.api.response.Character
import com.dboy.rickandmortyapp.paging.CharactersPagingSource
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val rmApi: RickAndMortyAPI,
) {
    fun getCharactersWithPagination(
        nameQuery: String = "",
        statusQuery: String = ""
    ): LiveData<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharactersPagingSource(rmApi, nameQuery, statusQuery)
            }
        ).liveData
    }
}