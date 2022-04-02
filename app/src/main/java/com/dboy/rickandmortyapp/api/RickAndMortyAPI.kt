package com.dboy.rickandmortyapp.api

import com.dboy.rickandmortyapp.api.response.ApiResponse
import com.dboy.rickandmortyapp.api.response.character.Character
import com.dboy.rickandmortyapp.api.response.episode.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character/")
    suspend fun getCharacters(
        @Query("name") nameQuery: String = "",
        @Query("page") page: Int
    ): Response<ApiResponse<Character>> //old Response<CharactersResponse>

    @GET("character/{id}")
    suspend fun getSingleCharacter(
        @Path("id") id: Int
    ): Response<Character>

    @GET("character/")
    suspend fun getFilteredCharacters(
        @Query("name") nameQuery: String,
        @Query("status") statusQuery: String,
        @Query("gender") genderQuery: String,
        @Query("page") page: Int
    ): Response<ApiResponse<Character>>

    @GET("episode/")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): Response<ApiResponse<Episode>>
}