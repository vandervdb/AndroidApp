package org.vander.spotifyclient.domain.state

sealed class SpotifyRemoteClientState {
    object NotConnected : SpotifyRemoteClientState()
    object Connecting : SpotifyRemoteClientState()
    object Connected : SpotifyRemoteClientState()
    data class Failed(val error: Exception) : SpotifyRemoteClientState()
}