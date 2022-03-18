package com.dboy.rickandmortyapp.repository

import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.database.CharacterDAO
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val rmApi: RickAndMortyAPI,
    private val characterDAO: CharacterDAO
) {

    //lidar com a paginação aqui
}