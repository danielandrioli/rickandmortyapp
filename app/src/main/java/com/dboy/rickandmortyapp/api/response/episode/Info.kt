package com.dboy.rickandmortyapp.api.response.episode

data class Info(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: String?
)