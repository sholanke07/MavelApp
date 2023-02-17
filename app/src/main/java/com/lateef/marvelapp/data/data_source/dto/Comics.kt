package com.lateef.marvelapp.data.data_source.dto

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)