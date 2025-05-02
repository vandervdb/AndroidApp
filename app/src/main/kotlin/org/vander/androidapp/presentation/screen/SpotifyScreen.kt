package org.vander.androidapp.presentation.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.vander.androidapp.presentation.components.MiniPlayer
import org.vander.androidapp.presentation.viewmodel.SpotifyViewModel
import org.vander.spotifyclient.domain.state.SpotifySessionState


@Composable
fun SpotifyScreen(
    navController: NavController,
    viewModel: SpotifyViewModel,
    launcher: ActivityResultLauncher<Intent>,
    activity: Activity?,
    modifier: Modifier
) {
    val sessionState by viewModel.sessionState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.requestAuthorization(launcher)
        viewModel.launchAuthorizationFlow(activity!!)
    }


    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        when (sessionState) {
            is SpotifySessionState.Idle,
            is SpotifySessionState.Authorizing,
            is SpotifySessionState.ConnectingRemote -> {
                CircularProgressIndicator()
            }

            is SpotifySessionState.Ready -> {
                Text("✅ Connecté à Spotify Remote !")
            }

            is SpotifySessionState.Failed -> {
                Text(
                    text = "❌ Erreur: ${(sessionState as SpotifySessionState.Failed).exception.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }

            SpotifySessionState.IsPaused -> TODO()
        }
        MiniPlayer(viewModel)
    }

    if (sessionState is SpotifySessionState.Failed) {
        Button(
            onClick = { activity?.let { viewModel.launchAuthorizationFlow(it) } },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("Réessayer")
        }
    }
}
