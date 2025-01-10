package com.example.emoji.data

import retrofit2.http.GET


const val BASE_URL = "https://api.emojisworld.fr/"

interface EmojiApi {
    @GET("v1/random")
    suspend fun getEmoji(): EmojiResponse

    @GET("v1/categories")
    suspend fun getCategories(): CategoriesResponse

    @GET("v1/popular")
    suspend fun getPopular(): EmojiResponse
}
