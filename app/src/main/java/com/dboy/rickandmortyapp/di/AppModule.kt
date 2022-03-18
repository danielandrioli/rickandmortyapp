package com.dboy.rickandmortyapp.di

import android.content.Context
import androidx.room.Room
import com.dboy.rickandmortyapp.api.RickAndMortyAPI
import com.dboy.rickandmortyapp.database.CharactersDatabase
import com.dboy.rickandmortyapp.util.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesNewsApi(retrofit: Retrofit): RickAndMortyAPI = retrofit.create(RickAndMortyAPI::class.java)

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): CharactersDatabase = Room
        .databaseBuilder(context, CharactersDatabase::class.java, "character_db.db")
        .build()

    @Singleton
    @Provides
    fun providesCharacterDao(db: CharactersDatabase) = db.characterDao()
}