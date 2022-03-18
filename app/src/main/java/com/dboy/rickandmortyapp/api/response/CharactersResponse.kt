package com.dboy.rickandmortyapp.api.response

data class CharactersResponse(
    val info: Info,
    val results: List<Character>
)