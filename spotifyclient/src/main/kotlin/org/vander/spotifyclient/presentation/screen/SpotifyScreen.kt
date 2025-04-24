package org.vander.spotifyclient.presentation.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.vander.spotifyclient.data.client.auth.ConnectionState
import org.vander.spotifyclient.presentation.viewmodel.SpotifyViewModel


@Composable
fun SpotifyScreen(
    navController: NavController,
    spotifyViewModel: SpotifyViewModel = viewModel(),
    activity: Activity,
    modifier: Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { spotifyViewModel.setStateFromConnectionResult(it) }
    )
    val state by spotifyViewModel.connectionState.collectAsState()
    val image by spotifyViewModel.trackImage.collectAsState()

    LaunchedEffect(key1 = Unit) {
        activity?.let {
            spotifyViewModel.connectToSpotify(it, launcher)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .displayCutoutPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Spotify Screen")

        when (state) {
            is ConnectionState.Connected -> {
                Text(text = "Connected to Spotify with token: ${(state as ConnectionState.Connected).accessToken}")
            }

            is ConnectionState.Disconnected -> {
                Text(text = "Disconnected from Spotify")
            }

            is ConnectionState.Connecting -> {
                SpotifyConnectingProgress()
            }

            is ConnectionState.Failed -> {
                Text(text = "Failed to connect to Spotify")
            }
        }

        Button(
            onClick = { spotifyViewModel.play() },
            content = { Text(text = "Play") },
            enabled = state is ConnectionState.Connected
        )

        image?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Pochette",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
        } ?: Text("Chargement de l’image...")

    }
}

@Composable
fun SpotifyConnectingProgress() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

