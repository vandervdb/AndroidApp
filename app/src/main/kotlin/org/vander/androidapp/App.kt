package org.vander.androidapp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.vander.androidapp.navigation.NavItem
import org.vander.androidapp.navigation.NavItem.Spotify
import org.vander.androidapp.presentation.components.NavigationRailWithNav
import org.vander.androidapp.presentation.screen.HomeScreen
import org.vander.androidapp.presentation.screen.SpotifyScreenWrapper
import org.vander.androidapp.presentation.viewmodel.SpotifyViewModel

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->

        Row(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavigationRailWithNav(navController = navController)
            NavHost(
                navController = navController,
                startDestination = NavItem.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(NavItem.Home.route) { HomeScreen() }
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
}

