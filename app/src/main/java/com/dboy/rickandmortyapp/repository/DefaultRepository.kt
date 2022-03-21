package com.dboy.rickandmortyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.api.response.Character
import com.dboy.rickandmortyapp.paging.CharactersPagingSource
import com.dboy.rickandmortyapp.util.Resource
import retrofit2.Response
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

    suspend fun getSingleCharacter(id: Int): Resource<Character> {
        val response = rmApi.getSingleCharacter(id)
        val result = response.body()
        return try {
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            } else {
                Resource.Error(message = response.message(), result)
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString(), result)
        }
    }
}