package org.vander.spotifyclient.domain.state

sealed class SpotifySessionState {
    object Idle : SpotifySessionState()
    object Authorizing : SpotifySessionState()
    object ConnectingRemote : SpotifySessionState()
    object Ready : SpotifySessionState()
    object IsPaused : SpotifySessionState()
    data class Failed(val exception: Throwable) : SpotifySessionState()
}