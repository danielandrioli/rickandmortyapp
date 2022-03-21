package com.dboy.rickandmortyapp.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dboy.rickandmortyapp.api.response.Character
import com.dboy.rickandmortyapp.repository.DefaultRepository
import com.dboy.rickandmortyapp.util.CharacterQuery
import com.dboy.rickandmortyapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RmViewModel @Inject constructor(private val repository: DefaultRepository) : ViewModel() {
    private val query = MutableLiveData<CharacterQuery>()
    val charactersWithPagination = query.switchMap {
        repository.getCharactersWithPagination(it.name, it.status).cachedIn(viewModelScope)
    }
    private val _character: MutableLiveData<Resource<Character>> = MutableLiveData()
    val character: LiveData<Resource<Character>> = _character

    init {
        query.value = CharacterQuery()
    }

    fun makeCharacterQuery(name: String, status: String = "") {
        query.value = CharacterQuery(name, status)
    }

    fun getSingleCharacter(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _character.postValue(Resource.Loading())
            val result = repository.getSingleCharacter(id)
            _character.postValue(result)
        }
    }

    fun setValueCharacterNull(){
        _character.value = null
    }
}
