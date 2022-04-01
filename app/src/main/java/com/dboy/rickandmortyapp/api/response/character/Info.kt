package com.dboy.rickandmortyapp.api.response.character

data class Info(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: Any?
)