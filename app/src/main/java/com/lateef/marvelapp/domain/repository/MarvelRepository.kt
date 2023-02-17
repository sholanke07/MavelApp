package com.lateef.marvelapp.domain.repository

import com.lateef.marvelapp.data.data_source.dto.CharactersDTO

interface MarvelRepository {

    suspend fun getAllCharacter(offset: Int): CharactersDTO

    suspend fun getAllSearchCharacters(search: String): CharactersDTO
}