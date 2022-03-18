package com.dboy.rickandmortyapp.api.response

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Location(
    val name: String,
    val url: String
) : Parcelable