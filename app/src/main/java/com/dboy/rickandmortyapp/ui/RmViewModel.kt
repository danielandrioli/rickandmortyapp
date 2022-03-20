package com.dboy.rickandmortyapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dboy.rickandmortyapp.repository.DefaultRepository
import com.dboy.rickandmortyapp.util.CharacterQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RmViewModel @Inject constructor(private val repository: DefaultRepository): ViewModel() {
    private val query = MutableLiveData<CharacterQuery>()
    val charactersWithPagination = query.switchMap {
        repository.getCharactersWithPagination(it.name, it.status).cachedIn(viewModelScope)
    }

    init {
        query.value = CharacterQuery()
    }

    fun makeCharacterQuery(name: String, status: String = "") {
        query.value = CharacterQuery(name, status)
    }
}
