package com.dboy.rickandmortyapp.api.response

data class ApiResponse<T>(
    val info: Info,
    val results: List<T>
)
