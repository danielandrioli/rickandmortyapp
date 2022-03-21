package com.dboy.rickandmortyapp.api

import com.dboy.rickandmortyapp.api.response.Character
import com.dboy.rickandmortyapp.api.response.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character/")
    suspend fun getCharacters(
        @Query("name") nameQuery: String = "",
        @Query("status") status: String = "",   //empty means all status: dead, alive and unknown
        @Query("page") page: Int
    ): Response<CharactersResponse>

    @GET("character/{id}")
    suspend fun getSingleCharacter(
        @Path("id") id: Int
    ) : Response<Character>
}