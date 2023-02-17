package com.lateef.marvelapp.ui.characterList

import com.lateef.marvelapp.domain.model.Character

data class MarvelListState(
    val isLoading: Boolean = false,
    val characterList: List<Character> = emptyList(),
    val error: String = ""
)
