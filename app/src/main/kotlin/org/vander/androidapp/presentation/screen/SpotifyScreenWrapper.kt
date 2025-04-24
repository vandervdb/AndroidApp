package org.vander.androidapp.presentation.screen

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.vander.spotifyclient.presentation.screen.SpotifyScreen
import org.vander.spotifyclient.presentation.viewmodel.SpotifyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyScreenWrapper(navController: NavController, spotifyViewModel: SpotifyViewModel, activity : Activity) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spotify") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Action Play */ }
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        },
        floatingActionButtonPosition = FabPosition.End,

    ) { innerPadding ->
        SpotifyScreen(
            navController = navController,
            spotifyViewModel = spotifyViewModel,
            activity = activity,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
