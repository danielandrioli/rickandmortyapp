package com.dboy.rickandmortyapp.paging

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.api.response.episode.Episode
import com.dboy.rickandmortyapp.util.CHARACTERS_STARTING_PAGE
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.CancellationException

class EpisodesPagingSource(
    private val rmApi: RickAndMortyAPI
): PagingSource<Int, Episode>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        val page = params.key ?: CHARACTERS_STARTING_PAGE
        Log.i("EpisodesPaging", "dentro do load")


        return try {
            val response = rmApi.getEpisodes(page)
            val episodesResponse = response.body()
            if (response.isSuccessful && episodesResponse != null) {
                Log.i("EpisodesPaging", "successfull response: $response")

                val nextPageNumber: Int? = if (episodesResponse.info.next != null){
                    val uri = Uri.parse(episodesResponse.info.next)
                    val nextPageQuery = uri.getQueryParameter("page")
                    nextPageQuery?.toInt()
                } else {
                    null
                }

                PagingSource.LoadResult.Page(
                    data = episodesResponse.results,
                    prevKey = null,
                    nextKey = nextPageNumber
                )
            } else {
                Log.i("EpisodesPaging", "Erro: $response")
                throw Exception(response.code().toString())
            }
        } catch (exception: IOException) {
            PagingSource.LoadResult.Error(exception)
        } catch (exception: HttpException) {
            PagingSource.LoadResult.Error(exception)
        } catch (exception: Exception) {
            Log.i("EpisodesPaging", "Erro geral: $exception")
            if (exception is CancellationException) throw exception
            PagingSource.LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        return state.anchorPosition
    }
}