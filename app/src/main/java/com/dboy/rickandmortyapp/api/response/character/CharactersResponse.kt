package com.dboy.rickandmortyapp.api.response.character

data class CharactersResponse(
    val info: Info,
    val results: List<Character>
)