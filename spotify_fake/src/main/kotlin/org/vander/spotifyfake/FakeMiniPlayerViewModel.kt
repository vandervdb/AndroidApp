package org.vander.spotifyfake

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.MutableStateFlow
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.coreui.IMiniPlayerViewModel


class FakeMiniPlayerViewModel : IMiniPlayerViewModel {
    override val sessionState = MutableStateFlow<SpotifySessionState>(SpotifySessionState.Ready)
    override val playerStateData = MutableStateFlow<PlayerStateData>(
        PlayerStateData(
            trackName = "Zelda's Theme",
            artistName = "Koji Kondo",
            trackId = null,
            isPaused = true,
            playing = false,
            paused = true,
            stopped = false,
            shuffling = false,
            repeating = true,
            seeking = false,
            skippingNext = false,
            skippingPrevious = false
        )
    )

    override fun togglePlayPause() {
        val current = playerStateData.value!!
        playerStateData.value = current.copy(isPaused = !current.isPaused)
    }

    override fun isPlaying(): Boolean = playerStateData.value?.isPaused == false

    override fun requestAuthorization(launcher: ActivityResultLauncher<Intent>) {
        // Nothing to do
    }

    override fun launchAuthorizationFlow(activity: Activity) {
        // Nothing to do
    }

    override fun disconnectSpotify() {
        // Nothing to do
    }
}