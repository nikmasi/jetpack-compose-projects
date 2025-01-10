package com.example.emoji.data

import javax.inject.Inject

class EmojiRepository @Inject constructor(
    private val emojiApi: EmojiApi,
) {
    suspend fun getEmojis(): EmojiResponse =
        emojiApi.getEmoji()

    suspend fun getCategories(): CategoriesResponse =
        emojiApi.getCategories()

    suspend fun getPopular(): EmojiResponse =
        emojiApi.getPopular()
}
