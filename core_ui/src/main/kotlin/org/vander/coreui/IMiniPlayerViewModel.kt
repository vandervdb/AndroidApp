package org.vander.coreui

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.SpotifyPlayerState
import org.vander.spotifyclient.domain.state.SpotifySessionState
import org.vander.spotifyclient.domain.state.UIQueueState

interface IMiniPlayerViewModel {
    val sessionState: StateFlow<SpotifySessionState>
    val uIQueueState: StateFlow<UIQueueState>
    val spotifyPlayerState: StateFlow<SpotifyPlayerState>
    fun startUp(activity: Activity)
    fun togglePlayPause()
    fun skipNext()
    fun skipPrevious()
    fun playTrack(trackId: String)
    fun checkIfTrackSaved(trackId: String)
    fun toggleSaveTrack(trackId: String)
}