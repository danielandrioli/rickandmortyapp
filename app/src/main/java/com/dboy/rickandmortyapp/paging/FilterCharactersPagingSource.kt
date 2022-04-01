package com.dboy.rickandmortyapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.api.response.character.Character
import com.dboy.rickandmortyapp.util.CHARACTERS_STARTING_PAGE

class FilterCharactersPagingSource(
    private val rmApi: RickAndMortyAPI,
    private val nameQuery: String,
    private val statusQuery: String,
    private val genderQuery: String
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: CHARACTERS_STARTING_PAGE

        return loadResponse {
            rmApi.getFilteredCharacters(
                nameQuery = nameQuery,
                statusQuery = statusQuery,
                genderQuery = genderQuery,
                page = page
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }
}