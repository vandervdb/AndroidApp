package org.vander.spotifyclient.data.model

sealed class SpotifyRemoteClientState {
    object NotConnected : SpotifyRemoteClientState()
    object Connecting : SpotifyRemoteClientState()
    object Connected : SpotifyRemoteClientState()
}