package com.lateef.marvelapp.ui.characterList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lateef.marvelapp.domain.use_cases.CharacterUseCase
import com.lateef.marvelapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val charactersUseCase: CharacterUseCase): ViewModel(){

    private val marvelValue = MutableStateFlow(MarvelListState())
    var _marvelValue: StateFlow<MarvelListState> = marvelValue

    fun getAllCharactersData(offset: Int) = viewModelScope.launch(Dispatchers.IO) {
        charactersUseCase(offset = offset).collect {
            when(it){
                is Response.Success ->{
                    marvelValue.value = MarvelListState(characterList = it.data?: emptyList())
                }
                is Response.Loading ->{
                    marvelValue.value = MarvelListState(isLoading = true)
                }
                is Response.Error ->{
                    marvelValue.value = MarvelListState(error = it.message?: "An Unexpected Error")
                }
            }
        }
    }
}