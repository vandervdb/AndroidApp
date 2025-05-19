package org.vander.androidapp.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vander.androidapp.presentation.components.preview.PreviewMiniPlayerWithLocalCover
import org.vander.coreui.IMiniPlayerViewModel
import org.vander.spotifyclient.domain.data.UIQueueItem
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.state.artistName
import org.vander.spotifyclient.domain.state.coverId
import org.vander.spotifyclient.domain.state.isPaused
import org.vander.spotifyclient.domain.state.trackId
import org.vander.spotifyclient.domain.state.trackName

@Composable
fun MiniPlayer(viewModel: IMiniPlayerViewModel) {
    val sessionState by viewModel.sessionState.collectAsState()
    val playerState by viewModel.spotifyPlayerState.collectAsState()
    val uIQueueState by viewModel.uIQueueState.collectAsState()

    if (sessionState is SpotifySessionState.Ready) {
        MiniPlayerContent(
            tracksQueue = uIQueueState.items,
            trackName = playerState.trackName,
            artistName = playerState.artistName,
            trackId = playerState.trackId,
            isSaved = playerState.isTrackSaved == true,
            isPaused = playerState.isPaused,
            saveTrack = {
                Log.d("MiniPlayer", "Saving track: $it")
                viewModel.toggleSaveTrack(it)
            },
            playTrack = {
                Log.d("MiniPlayer", "Playing track: $it")
                viewModel.playTrack(it)
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
    tracksQueue: List<UIQueueItem>,
    trackName: String,
    artistName: String,
    trackId: String = "",
    isSaved: Boolean = false,
    isPaused: Boolean,
    onPlayPause: () -> Unit,
    saveTrack: (String) -> Unit,
    playTrack: (String) -> Unit,
    cover: @Composable () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { tracksQueue.size })
    val currentTrackId = trackId

    LaunchedEffect(currentTrackId) {
        val index = tracksQueue.indexOfFirst { it.trackId == currentTrackId }
        if (index >= 0) {
            pagerState.animateScrollToPage(index)
        }
    }

//    LaunchedEffect(pagerState.currentPage) {
//        val newTrackId = tracksQueue.getOrNull(pagerState.currentPage)?.trackId
//        if (newTrackId != null) {
//            Log.d("MiniPlayer", "Swiped to trackId=$newTrackId")
//            playTrack(newTrackId)
//        }
//    }

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

            Box(modifier = Modifier.weight(1f)) {
                TracksQueue(pagerState, tracksQueue)
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
private fun TracksQueue(pagerState: PagerState, tracksQueue: List<UIQueueItem>) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
        pageSpacing = 8.dp
    ) { index ->
        val item = tracksQueue[index]
        TrackItem(
            trackName = item.trackName,
            artistName = item.artistName
        )
    }
}

@Composable
private fun TrackItem(trackName: String, artistName: String) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        MarqueeText(
            text = trackName,
            modifier = Modifier.width(120.dp),
            durationMillis = 10000
        )
        Text(
            text = artistName,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}


@Composable
fun MiniPlayerWithPainter(
    viewModel: IMiniPlayerViewModel,
    coverPainter: Painter
) {
    val sessionState by viewModel.sessionState.collectAsState()
    val playerState by viewModel.spotifyPlayerState.collectAsState()
    val uIQueueState by viewModel.uIQueueState.collectAsState()

    if (sessionState is SpotifySessionState.Ready) {
        MiniPlayerContent(
            tracksQueue = uIQueueState.items,
            trackName = playerState.trackName,
            artistName = playerState.artistName,
            trackId = playerState.trackId,
            isSaved = playerState.isTrackSaved == true,
            isPaused = playerState.isPaused,
            saveTrack = {
                Log.d("MiniPlayer", "Saving track: $it")
                viewModel.toggleSaveTrack(it)
            },
            playTrack = {
                Log.d("MiniPlayer", "Playing track: $it")
                viewModel.playTrack(it)
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
