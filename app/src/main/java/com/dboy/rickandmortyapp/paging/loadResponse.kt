package com.dboy.rickandmortyapp.paging

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import com.dboy.rickandmortyapp.api.response.ApiResponse
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.CancellationException

suspend fun <T : Any> loadResponse(getResponse: suspend () -> Response<ApiResponse<T>>): PagingSource.LoadResult<Int, T> {
    return try {
        val response = getResponse()
        val bodyResponse = response.body()
        if (response.isSuccessful && bodyResponse != null) {
            Log.i("Paging", "successfull response: $response")

            val nextPageNumber: Int? = if (bodyResponse.info.next != null) {
                val uri = Uri.parse(bodyResponse.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageQuery?.toInt()
            } else {
                null
            }

            PagingSource.LoadResult.Page(
                data = bodyResponse.results,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } else {
            Log.i("Paging", "Erro: $response")
            throw Exception(response.code().toString())
        }
    } catch (exception: IOException) {
        PagingSource.LoadResult.Error(exception)
    } catch (exception: HttpException) {
        PagingSource.LoadResult.Error(exception)
    } catch (exception: Exception) {
        Log.i("Paging", "Erro geral: $exception")
        if (exception is CancellationException) throw exception
        PagingSource.LoadResult.Error(exception)
    }
}