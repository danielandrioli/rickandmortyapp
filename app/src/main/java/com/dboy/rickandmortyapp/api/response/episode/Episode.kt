package com.dboy.rickandmortyapp.api.response.episode

data class Episode(
    val air_date: String,
    val characters: List<String>, //list of characters url
    val created: String,
    val episode: String, //e.g: S01E01
    val id: Int,
    val name: String, //"Pilot"
    val url: String
)