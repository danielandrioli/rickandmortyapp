package com.dboy.rickandmortyapp.paging

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import com.dboy.rickandmortyapp.api.response.Character
import com.dboy.rickandmortyapp.api.response.CharactersResponse
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.CancellationException

suspend fun loadResponse(getResponse: suspend () -> Response<CharactersResponse>) : PagingSource.LoadResult<Int, Character> {
    return try {
        val response = getResponse()
        val charactersResponse = response.body()
        if (response.isSuccessful && charactersResponse != null) {
            Log.i("CharactersPaging", "successfull response: $response")

            val nextPageNumber: Int? = if (charactersResponse.info.next != null){
                val uri = Uri.parse(charactersResponse.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageQuery?.toInt()
            } else {
                null
            }

            PagingSource.LoadResult.Page(
                data = charactersResponse.results,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } else {
            Log.i("CharactersPaging", "Erro: $response")
            throw Exception(response.code().toString())
        }
    } catch (exception: IOException) {
        PagingSource.LoadResult.Error(exception)
    } catch (exception: HttpException) {
        PagingSource.LoadResult.Error(exception)
    } catch (exception: Exception) {
        Log.i("CharactersPaging", "Erro geral: $exception")
        if (exception is CancellationException) throw exception
        PagingSource.LoadResult.Error(exception)
    }
}