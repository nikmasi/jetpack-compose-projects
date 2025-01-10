package com.example.emoji

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emoji.ui.theme.EmojiTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emoji.screens.CategoriesScreen
import com.example.emoji.screens.Destinations
import com.example.emoji.screens.HomeScreen
import com.example.emoji.screens.PopularScreen
import com.example.emoji.screens.RandomScreen
import com.example.emoji.screens.listDestinations
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmojiTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(){
    val viewModel: EmojiViewModel = hiltViewModel()
    val categoryViewModel:CategoryViewModel= hiltViewModel()

    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: Destinations.Home.route


    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
                title = {
                    Text(
                        if (currentRoute==Destinations.Home.route)"Home"
                        else if (currentRoute==Destinations.Random.route) "Random"
                        else if (currentRoute==Destinations.Popular.route) "Popular"
                        else "Categories",
                                maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },
        bottomBar = {
            NavigationBar {
                listDestinations.forEach { navDestination ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = navDestination.icon,
                                contentDescription = null,
                            )
                        },
                        label = { Text(text = navDestination.name) },
                        selected = currentRoute.startsWith(navDestination.route),
                        onClick = {

                            navController.navigate(navDestination.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(Destinations.Home.route) {
                                    saveState = true
                                    inclusive = false
                                }
                            }
                        },
                    )
                }
            }
        }

    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Destinations.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Destinations.CategoriesScreen.route) {
                CategoriesScreen(viewModel=categoryViewModel)
            }
            composable(Destinations.Home.route) {
                HomeScreen()
            }
            composable(Destinations.Random.route) {
                RandomScreen(viewModel=viewModel)
            }
            composable(Destinations.Popular.route) {
                PopularScreen(viewModel=viewModel)
            }
        }
    }
}
