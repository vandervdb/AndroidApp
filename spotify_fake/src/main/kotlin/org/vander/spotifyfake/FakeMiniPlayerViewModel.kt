package org.vander.spotifyfake

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.MutableStateFlow
import org.vander.coreui.IMiniPlayerViewModel
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifyPlayerState
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.state.UIQueueState
import org.vander.spotifyclient.domain.state.togglePause


class FakeMiniPlayerViewModel : IMiniPlayerViewModel {
    override val sessionState = MutableStateFlow<SpotifySessionState>(SpotifySessionState.Ready)
    override val uIQueueState = MutableStateFlow<UIQueueState>(UIQueueState.empty())
    override val spotifyPlayerState = MutableStateFlow<SpotifyPlayerState>(
        SpotifyPlayerState(
            base = PlayerStateData(
                trackName = "Zelda's Theme",
                artistName = "Koji Kondo",
                coverId = "",
                trackId = "",
                isPaused = true,
                playing = false,
                paused = true,
                stopped = false,
                shuffling = false,
                repeating = true,
                seeking = false,
                skippingNext = false,
                skippingPrevious = false
            ),
            isTrackSaved = false
        )
    )

    override fun startSpotifyClient(
        launcher: ActivityResultLauncher<Intent>,
        activity: Activity
    ) {
        TODO("Not yet implemented")
    }

    override fun shutDownSpotifyClient() {
        TODO("Not yet implemented")
    }

    override fun togglePlayPause() {
        spotifyPlayerState.togglePause()
    }

    override fun playTrack(trackId: String) {
        TODO("Not yet implemented")
    }

    override fun checkIfTrackSaved(trackId: String) {
        // Nothing to do
    }

    override fun toggleSaveTrack(trackId: String) {
        // Nothing to do
    }
}