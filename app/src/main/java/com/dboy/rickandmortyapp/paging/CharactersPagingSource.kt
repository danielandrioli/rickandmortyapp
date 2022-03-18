package com.dboy.rickandmortyapp.paging

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.api.response.Character
import com.dboy.rickandmortyapp.util.CHARACTERS_STARTING_PAGE
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.CancellationException

class CharactersPagingSource(
    private val rmApi: RickAndMortyAPI,
    private val nameQuery: String = "",
    private val statusQuery: String = "",
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: CHARACTERS_STARTING_PAGE

        return try {
            val response = rmApi.getCharacters(nameQuery = nameQuery, status = statusQuery, page = page)
            val charactersResponse = response.body()
            if (response.isSuccessful && charactersResponse != null) {
                Log.i("CharactersPaging", "successfull response: $response")

                val nextPageNumber: Int?
                val uri = Uri.parse(charactersResponse.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()

                LoadResult.Page(
                    data = charactersResponse.results,
                    prevKey = null,
                    nextKey = nextPageNumber
                )
            } else {
                Log.i("BreakingNewsPaging", "Erro: $response")
                throw Exception(response.code().toString())
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }
}