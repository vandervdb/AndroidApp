package org.vander.coreui

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.StateFlow
import org.vander.spotifyclient.domain.state.PlayerStateData
import org.vander.spotifyclient.domain.state.SpotifySessionState

interface IMiniPlayerViewModel {
    val sessionState: StateFlow<SpotifySessionState>
    val playerStateData: StateFlow<PlayerStateData>
    fun requestAuthorization(launcher: ActivityResultLauncher<Intent>)
    fun launchAuthorizationFlow(activity: Activity)
    fun togglePlayPause()
    fun isPlaying(): Boolean
}