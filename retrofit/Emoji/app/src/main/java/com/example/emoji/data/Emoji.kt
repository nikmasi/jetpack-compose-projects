package com.example.emoji.data

data class Emoji(
    val id: Int,
    val name: String,
    val emoji: String,
    val unicode: String,
    val version: String,
    val category: Category,
    val sub_category: SubCategory,
    val children: List<Child> // List of children if any
)

data class SubCategory(
    val id: Int,
    val name: String,
    val emojis_count: Int
)

data class Category(
    val id: Int,
    val name: String,
    val emojis_count: Int,
    val sub_categories: List<SubCategory>
)

data class CategoriesResponse(
    val totals: Int,
    val results: List<Category>
)

data class Child(
    val id: Int,
    val name: String,
    val emoji: String,
    val unicode: String
)

data class EmojiResponse(
    val totals: Int,
    val results: List<Emoji> // This holds the list of Emoji objects
)