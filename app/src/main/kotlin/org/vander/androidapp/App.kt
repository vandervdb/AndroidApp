package org.vander.androidapp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.vander.androidapp.navigation.BottomNavItem
import org.vander.androidapp.navigation.BottomNavItem.Spotify
import org.vander.androidapp.presentation.screen.HomeScreen
import org.vander.androidapp.presentation.screen.SpotifyScreenWrapper
import org.vander.spotifyclient.presentation.screen.SpotifyScreen
import org.vander.spotifyclient.presentation.viewmodel.SpotifyViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    val items = listOf(BottomNavItem.Home, Spotify)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackEntry?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(Spotify.route) {
                val viewModel = hiltViewModel<SpotifyViewModel>()
                val activity = LocalContext.current as MainActivity
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = { result ->
                        activity?.let {
                            viewModel.handleAuthResult(it, result)
                        }
                    }
                )
                SpotifyScreenWrapper(
                    navController = navController,
                    spotifyViewModel = viewModel,
                    launcher = launcher,
                    activity = activity
                )
            }
        }
    }

}