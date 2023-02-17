package com.lateef.marvelapp.ui.characterList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lateef.marvelapp.domain.use_cases.SearchCharacterUseCase
import com.lateef.marvelapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCharactersViewModel @Inject constructor(private val searchUseCase: SearchCharacterUseCase): ViewModel() {

    private val marvelValueSearch = MutableStateFlow(MarvelListState())
    var _marvelValueSearch: StateFlow<MarvelListState> = marvelValueSearch

    fun getSearchCharacters(search: String) = viewModelScope.launch(Dispatchers.IO) {
        searchUseCase.invoke(search = search).collect {
            when(it){
                is Response.Success ->{
                    marvelValueSearch.value = MarvelListState(characterList = it.data?: emptyList())
                }
                is Response.Loading ->{
                    marvelValueSearch.value = MarvelListState(isLoading = true)
                }
                is Response.Error ->{
                    marvelValueSearch.value = MarvelListState(error = it.message?: "An Unexpected Error")
                }
            }
        }
    }
}