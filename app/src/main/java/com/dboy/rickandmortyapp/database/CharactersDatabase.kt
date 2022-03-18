package com.dboy.rickandmortyapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dboy.rickandmortyapp.api.response.Character

@TypeConverters(Converters::class)
@Database(entities = [Character::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDAO
}