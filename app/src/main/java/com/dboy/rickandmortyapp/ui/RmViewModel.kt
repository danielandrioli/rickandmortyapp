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
    val query = MutableLiveData<String>()
    val charactersWithPagination = query.switchMap {
        repository.getCharactersWithPagination(it).cachedIn(viewModelScope)
    }
    private val _character: MutableLiveData<Resource<Character>> = MutableLiveData()
    val character: LiveData<Resource<Character>> = _character

    init {
        query.value = ""
    }

    fun makeCharacterQuery(name: String) {
        query.value = name
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
