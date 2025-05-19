package org.vander.androidapp.presentation.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import org.vander.androidapp.presentation.components.LifecycleObserverComponent
import org.vander.androidapp.presentation.components.MiniPlayer
import org.vander.coreui.IMiniPlayerViewModel
import org.vander.spotifyclient.domain.state.SpotifySessionState


@Composable
fun SpotifyScreen(
    navController: NavController,
    viewModel: IMiniPlayerViewModel,
    launcher: ActivityResultLauncher<Intent>,
    activity: Activity?,
    modifier: Modifier
) {

    val tag = "SpotifyScreen"
    val sessionState by viewModel.sessionState.collectAsState()
    val uIQueueState by viewModel.uIQueueState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startSpotifyClient(launcher, activity!!)
    }

    LifecycleObserverComponent(tag, onStopCallback = viewModel::shutDownSpotifyClient)

    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Log.d(tag, "Session state: $sessionState")
        when (sessionState) {
            is SpotifySessionState.Idle -> {
                Log.d(tag, "Idle...")
            }

            is SpotifySessionState.Authorizing -> {
                Log.d(tag, "Authorizing...")
            }

            is SpotifySessionState.ConnectingRemote -> {
                Log.d(tag, "Connecting to remote...")
                CircularProgressIndicator()
            }

            is SpotifySessionState.Ready -> {
                Text("✅ Connecté à Spotify Remote !")
                MiniPlayer(viewModel)
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

// TODO add button to relaunch authorization flow

}
