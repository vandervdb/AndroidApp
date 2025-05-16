package org.vander.coreui

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.data.SpotifyQueue
import org.vander.spotifyclient.domain.state.SpotifyPlayerState
import org.vander.spotifyclient.domain.state.SpotifySessionState

interface IMiniPlayerViewModel {
    val sessionState: StateFlow<SpotifySessionState>
    val spotifyPlayerState: StateFlow<SpotifyPlayerState>
    val currentUserQueue: StateFlow<SpotifyQueue?>
    fun startSpotifyClient(launcher: ActivityResultLauncher<Intent>, activity: Activity)
    fun shutDownSpotifyClient()
    fun togglePlayPause()
    fun checkIfTrackSaved(trackId: String)
    fun toggleSaveTrack(trackId: String)
}