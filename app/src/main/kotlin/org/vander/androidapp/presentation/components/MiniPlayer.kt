package org.vander.androidapp.presentation.components

import android.util.Log
import android.util.MutableBoolean
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import org.vander.androidapp.presentation.components.preview.PreviewMiniPlayerWithLocalCover
import org.vander.coreui.IMiniPlayerViewModel
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.state.artistName
import org.vander.spotifyclient.domain.state.coverId
import org.vander.spotifyclient.domain.state.isPaused
import org.vander.spotifyclient.domain.state.playing
import org.vander.spotifyclient.domain.state.trackId
import org.vander.spotifyclient.domain.state.trackName

@Composable
fun MiniPlayer(viewModel: IMiniPlayerViewModel) {
    val sessionState by viewModel.sessionState.collectAsState()
    val playerState by viewModel.spotifyPlayerState.collectAsState()
    val currentUserQueue by viewModel.currentUserQueue.collectAsState()
    Log.d("MiniPlayer", "sessionState state: $sessionState")
    Log.d("MiniPlayer", "playerState state: $playerState")

    if (sessionState is SpotifySessionState.Ready) {
        MiniPlayerContent(
            trackName = playerState.trackName,
            artistName = playerState.artistName,
            trackId = playerState.trackId,
            isSaved = playerState.isTrackSaved == true,
            isPaused = playerState.isPaused,
            saveTrack = {
                Log.d("MiniPlayer", "Saving track: $it")
                viewModel.toggleSaveTrack(it)
            },
            onPlayPause = { viewModel.togglePlayPause() },
            cover = {
                SpotifyTrackCover(
                    imageUri = playerState.coverId,
                    modifier = Modifier.size(48.dp)
                )
            }
        )
    }
}

@Composable
private fun MiniPlayerContent(
    trackName: String,
    artistName: String,
    trackId: String = "",
    isSaved: Boolean = false,
    isPaused: Boolean,
    onPlayPause: () -> Unit,
    saveTrack: (String) -> Unit,
    cover: @Composable () -> Unit
) {
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
            cover()

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trackName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
                Text(
                    text = artistName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            IconButton(onClick = { saveTrack(trackId) }) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                    contentDescription = if (isSaved) "Remove from saved library" else "Save to library"
                )
            }

            IconButton(onClick = onPlayPause) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = if (isPaused) "Plat" else "Pause"
                )
            }
        }
    }
}


@Composable
fun MiniPlayerWithPainter(
    viewModel: IMiniPlayerViewModel,
    coverPainter: Painter
) {
    val sessionState by viewModel.sessionState.collectAsState()
    val playerState by viewModel.spotifyPlayerState.collectAsState()

    if (sessionState is SpotifySessionState.Ready) {
        MiniPlayerContent(
            trackName = playerState.trackName,
            artistName = playerState.artistName,
            trackId = playerState.trackId,
            isSaved = playerState.isTrackSaved == true,
            isPaused = playerState.isPaused,
            saveTrack = {
                Log.d("MiniPlayer", "Saving track: $it")
                viewModel.toggleSaveTrack(it)
            },
            onPlayPause = { viewModel.togglePlayPause() },
            cover = {
                SpotifyTrackCover(
                    painter = coverPainter,
                    modifier = Modifier.size(48.dp)
                )
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MiniPlayerPreview() {
    PreviewMiniPlayerWithLocalCover()
}
