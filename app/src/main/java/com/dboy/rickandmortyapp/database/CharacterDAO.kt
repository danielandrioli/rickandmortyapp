package com.dboy.rickandmortyapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dboy.rickandmortyapp.api.response.Character

@Dao
interface CharacterDAO {

    @Query("SELECT * FROM character")
    fun getAllFavorites(): LiveData<List<Character>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)
}