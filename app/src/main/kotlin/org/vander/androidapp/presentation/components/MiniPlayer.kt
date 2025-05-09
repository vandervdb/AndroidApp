package org.vander.androidapp.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.vander.androidapp.presentation.viewmodel.SpotifyViewModel
import org.vander.spotifyclient.domain.state.SpotifySessionState


@Composable
fun MiniPlayer(viewModel: SpotifyViewModel) {

    val sessionState by viewModel.sessionState.collectAsState()
    val playerState by viewModel.playerStateData.collectAsState()

    if (sessionState is SpotifySessionState.Ready) {
        Surface(
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                SpotifyTrackCover(
                    imageUri = playerState?.trackId ?: "",
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.padding(horizontal = 8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = playerState?.trackName ?: "Unknown Track",
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1
                    )
                    Text(
                        text = playerState?.artistName ?: "Unknown Artist",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = { viewModel.togglePlayPause() }
                ) {
                    Icon(
                        imageVector = if (playerState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (viewModel.isPlaying()) "Pause" else "Play"
                    )
                }
            }
        }
    }
}
