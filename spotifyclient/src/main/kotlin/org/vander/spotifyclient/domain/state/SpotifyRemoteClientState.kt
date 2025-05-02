package org.vander.spotifyclient.domain.state

import com.spotify.android.appremote.api.SpotifyAppRemote

sealed class SpotifyRemoteClientState {
    object NotConnected : SpotifyRemoteClientState()
    object Connecting : SpotifyRemoteClientState()
    data class Connected(val appRemote: SpotifyAppRemote) : SpotifyRemoteClientState()
    data class Failed(val error: Exception) : SpotifyRemoteClientState()
}