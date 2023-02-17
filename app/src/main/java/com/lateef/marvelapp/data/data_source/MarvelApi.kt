package com.lateef.marvelapp.data.data_source

import com.lateef.marvelapp.data.data_source.dto.CharactersDTO
import com.lateef.marvelapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("/v1/public/characters")
    suspend fun getAllCharacters(
        @Query("apiKey")apiKey: String = Constants.API_KEY,
        @Query("ts")ts: String = Constants.timeStamp,
        @Query("hash")hash: String = Constants.hash(),
        @Query("offset")offset: String
    ): CharactersDTO


    @GET("/v1/public/characters")
    suspend fun getAllSearchedCharacters(
        @Query("apiKey")apiKey: String = Constants.API_KEY,
        @Query("ts")ts: String = Constants.timeStamp,
        @Query("hash")hash: String = Constants.hash(),
        @Query("nameSearchWith")search: String
    ): CharactersDTO
}