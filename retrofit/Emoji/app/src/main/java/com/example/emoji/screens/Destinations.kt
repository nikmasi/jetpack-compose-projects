package com.example.emoji.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(val name:String,val icon: ImageVector, val route: String){

    object CategoriesScreen: Destinations(
        name = "categories",
        icon = Icons.Default.MoreVert,
        route = "categories_screen"
    )
    object Home: Destinations(
        name = "home",
        icon = Icons.Default.Home,
        route = "home_screen"
    )
    object Random: Destinations(
        name = "random",
        icon = Icons.Default.Refresh,
        route = "random_screen"
    )
    object Popular: Destinations(
        name = "popular",
        icon = Icons.Default.FavoriteBorder,
        route = "popular_screen"
    )
}

val listDestinations = listOf(Destinations.Home,Destinations.Popular,
    Destinations.Random,Destinations.CategoriesScreen)