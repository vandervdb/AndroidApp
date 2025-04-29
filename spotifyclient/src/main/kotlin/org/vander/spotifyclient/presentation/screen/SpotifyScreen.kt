package org.vander.spotifyclient.presentation.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.vander.spotifyclient.data.model.SpotifySessionState
import org.vander.spotifyclient.presentation.viewmodel.SpotifyViewModel

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
    }

    if (sessionState is SpotifySessionState.Failed) {
        Button(
            onClick = { activity?.let { viewModel.launchAuthorizationFlow(it) } },
            modifier = Modifier
//                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Réessayer")
        }
    }
}
