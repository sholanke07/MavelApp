package com.lateef.marvelapp.data.repository

import com.lateef.marvelapp.data.data_source.MarvelApi
import com.lateef.marvelapp.data.data_source.dto.CharactersDTO
import com.lateef.marvelapp.domain.repository.MarvelRepository
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor(private val api: MarvelApi): MarvelRepository{

    override suspend fun getAllCharacter(offset: Int): CharactersDTO {
        return api.getAllCharacters(offset = offset.toString())
    }

    override suspend fun getAllSearchCharacters(search: String): CharactersDTO {
        return api.getAllSearchedCharacters(search = search)
    }
}