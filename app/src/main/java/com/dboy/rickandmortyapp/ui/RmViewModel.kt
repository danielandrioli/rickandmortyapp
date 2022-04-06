package com.dboy.rickandmortyapp.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.dboy.rickandmortyapp.api.response.character.Character
import com.dboy.rickandmortyapp.repository.DefaultRepository
import com.dboy.rickandmortyapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RmViewModel @Inject constructor(private val repository: DefaultRepository) : ViewModel() {
    val nameQuery = MutableLiveData<String>("")
    val charactersWithPagination = nameQuery.switchMap {
        if (filterGender.value != "" || filterStatus.value != "") {
            Log.i("RmViewModel", "através do filtro")
            repository.getFilteredCharactersWithPagination(
                it,
                statusQuery = filterStatus.value ?: "",
                genderQuery = filterGender.value ?: ""
            ).cachedIn(viewModelScope)
        } else {
            repository.getCharactersWithPagination(it).cachedIn(viewModelScope)
        }
    }
    private val _character: MutableLiveData<Resource<Character>> = MutableLiveData()
    val character: LiveData<Resource<Character>> = _character
    val filterGender: MutableLiveData<String> = MutableLiveData("")
    val filterStatus: MutableLiveData<String> = MutableLiveData("")
    val episodesWithPagination = repository.getEpisodesWithPagination().cachedIn(viewModelScope)

    fun makeFilteredQuery(status: String, gender: String) {
        filterStatus.value = status
        filterGender.value = gender
        nameQuery.value =
            nameQuery.value //this way, the switchMap is gonna activate and the request is gonna be made
    }

    fun clearFilter() {
        filterGender.value = ""
        filterStatus.value = ""
    }

    fun makeCharacterQuery(name: String) {
        nameQuery.value = name
    }

    fun getSingleCharacter(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _character.postValue(Resource.Loading())
            val result = repository.getSingleCharacter(id)
            _character.postValue(result)
        }
    }

    fun setValueCharacterNull() {
        _character.value = null
    }


}
