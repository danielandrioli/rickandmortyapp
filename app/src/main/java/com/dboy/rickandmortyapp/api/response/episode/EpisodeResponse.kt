package com.dboy.rickandmortyapp.api.response.episode

data class EpisodeResponse(
    val info: Info,
    val results: List<Episode>
)